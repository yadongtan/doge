package com.yadong.doge.rpc.loadbalance;

import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.invoker.Invoker;
import com.yadong.doge.registry.status.RpcStatus;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
* @author YadongTan
* @date 2022/9/1 14:22
* @Description 最短响应时间
* 已测试,可用
*/
public class ShortestResponseLoadBalance extends AbstractLoadBalance{
    @Override
    public HostInfo doSelect(List<HostInfo> hostInfos, Invoker invoker) {
        // 服务提供者的众数
        int length = hostInfos.size();
        // 用于记录最短的响应时间
        long shortestResponse = Long.MAX_VALUE;
        // 最短的响应时间的节点个数
        int shortestCount = 0;
        // 记录每个节点的最短响应时间
        int[] shortestIndexes = new int[length];
        // 每个节点的权重
        int[] weights = new int[length];
        // 最短响应时间的节点的总权重
        int totalWeight = 0;
        // 第一个节点最短响应时间的权重
        int firstWeight = 0;
        // 所有最短响应时间的节点是否具有相同的权重
        boolean sameWeight = true;
        // 遍历所有的服务提供者节点
        for (int i = 0; i < length; i++) {
            HostInfo hostInfo = hostInfos.get(i);
            RpcStatus rpcStatus = RpcStatus.getStatus(hostInfo.getHostAndPort(), invoker.getMethod().getName());
            // 计算成功的平均响应时间
            long succeededAverageElaspsed = rpcStatus.getSucceededMaxElapsed();
            // 计数器
            int actvie = rpcStatus.getActive();
            // 成功的平均响应时间 * 计数
            long estimateResponse = succeededAverageElaspsed * actvie;
            // 预热权重
            int afterWarmup = getWeight(hostInfo);
            weights[i] = afterWarmup;
            if(estimateResponse < shortestResponse){    //记录estimateResponse最小的对应节点信息
                shortestResponse = estimateResponse;
                shortestCount = 1;
                shortestIndexes[0] = i;
                totalWeight = afterWarmup;
                firstWeight = afterWarmup;
                sameWeight = true;
            }else if(estimateResponse == shortestResponse){ //若存在相同的estimateResponse,那么记录下节点的索引, 并更新totalWeight
                shortestIndexes[shortestCount++] = i;
                totalWeight += afterWarmup;
                if(sameWeight && i > 0 && afterWarmup != firstWeight){
                    sameWeight = false;
                }
            }
        }
        if(shortestCount == 1){ //如果只有一个最小的, 那么就返回它
            return hostInfos.get(shortestIndexes[0]);
        }
        if(!sameWeight && totalWeight > 0){ //如果estimateResponse最小的节点中,至少有一个节点的权重和别的不同, 并且, 总权重大于0
            int offsetWeight = ThreadLocalRandom.current().nextInt(totalWeight);    // 0 ~ totalWeight生成随机数
            for(int i = 0; i < shortestCount; i++){ //遍历estimateResponse最小的节点
                int shortestIndex = shortestIndexes[i];
                offsetWeight -= weights[shortestIndex]; // 更新随机数, 随机数 - 节点的权重
                if(offsetWeight < 0){   //若 小于0, 说明找到了服务提供者, 返回
                    return hostInfos.get(shortestIndex);
                }
            }
        }
        // 如果权重都相同, 或者都为0, 那么就从estimateResponse最小的节点当中, 随机的返回一个
        return hostInfos.get(shortestIndexes[ThreadLocalRandom.current().nextInt(shortestCount)]);
    }
}
