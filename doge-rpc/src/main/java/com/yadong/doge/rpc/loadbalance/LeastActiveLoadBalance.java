package com.yadong.doge.rpc.loadbalance;

import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.invoker.Invoker;
import com.yadong.doge.registry.status.RpcStatus;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
* @author YadongTan
* @date 2022/9/1 13:19
* @Description 最少连接
* 已测试,可用
*/
public class LeastActiveLoadBalance extends AbstractLoadBalance {

    @Override
    public HostInfo doSelect(List<HostInfo> hostInfos, Invoker invoker) {
        // 服务提供者节点总数
        int length = hostInfos.size();
        // 最少连接的节点的连接数
        int leastActive = -1;
        // 相同最少连接节点的个数
        int leastCount = 0;
        // 用于记录相同连接数节点的索引值
        int[] leastIndexes = new int[length];
        // 用于记录节点的权重
        int[] weights = new int[length];
        // 用于统计相同连接数节点的总权重
        int totalWeight = 0;
        // 相同连接数节点中第一个节点的权重
        int firstWeight = 0;
        boolean sameWeight = true;
        for (int i = 0; i < length; i++) {
            HostInfo hostInfo = hostInfos.get(i);
            // 得到连接数
            int active = RpcStatus.getActiveStatus(hostInfo.getHostAndPort(), invoker.getMethod().getName());
            int afterWarmup = 100;  //这里应该计算一下权重, 偷懒直接取100
            // 记录节点的权重值
            weights[i] = afterWarmup;
            if (leastActive == -1 || active < leastActive) {  //记录最小连接的节点信息
                // 最小连接数
                leastActive = active;
                // 重置相同最少连接节点的总数为1;
                leastCount = 1;
                // 放在数组的第一个位置
                leastIndexes[0] = i;
                // 重置相同连接数节点的总权重
                totalWeight = afterWarmup;
                // 重置相同连接数节点的第一个节点的总权重
                firstWeight = afterWarmup;
                sameWeight = true;
            } else if (active == leastActive) {    //连接数相同
                leastIndexes[leastCount++] = i; //放在前一个节点的后一个位置
                totalWeight += afterWarmup; //更新总权重
                // 判断所有连接数相同的节点, 是否具有相同的权重
                if (sameWeight && afterWarmup != firstWeight) {
                    sameWeight = false;
                }
            }
        }
        // 仅有一个就返回
        if (leastCount == 1) {
            return hostInfos.get(leastIndexes[0]);
        }
        if (!sameWeight && totalWeight > 0) { //如果 连接数相同的节点中, 至少有一个节点的权重和别的不同, 并且, 总权重大于0
            //得到随机数
            int offsetWeight = ThreadLocalRandom.current().nextInt(totalWeight);
            // 遍历连接数相同的节点, 更新随机数
            for (int i = 0; i < leastCount; i++) {
                int leastIndex = leastIndexes[i];
                offsetWeight -= weights[leastIndex];
                if (offsetWeight < 0) {   //返回找到的服务提供者
                    return hostInfos.get(leastIndex);
                }
            }
        }
        // 若所有连接数相同的节点的权重相同或都为0, 那么就从中随机返回一个
        return hostInfos.get(leastIndexes[ThreadLocalRandom.current().nextInt(leastCount)]);
    }

}
