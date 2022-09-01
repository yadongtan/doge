package com.yadong.doge.rpc.loadbalance;

import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.invoker.Invoker;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
* @author YadongTan
* @date 2022/9/1 11:50
* @Description 随机加权算法
*/
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    public HostInfo doSelect(List<HostInfo> hostInfos, Invoker invoker) {
        int length = hostInfos.size();  //服务提供者的数量
        boolean sameWeight = true;  //是否每一个节点的权重都相等
        int[] weights = new int[length]; //服务提供者的权重前缀和
        int totalWeight = 0;    //总权重
        for(int i = 0; i < length; i++){
            int weight = getWeight(hostInfos.get(i));
            totalWeight += weight;
            weights[i] = weight;
            if(sameWeight && totalWeight != weight * (i + 1)){  //判断是否所有节点权重都相同
                sameWeight = false;
            }
        }
        if(totalWeight > 0 && !sameWeight){ //总权重大于0, 也就是所有节点至少有一个分配了权重
            int offset = ThreadLocalRandom.current().nextInt(totalWeight);
            for(int i = 0; i < length; i++){
                if(offset < weights[i]){
                    return hostInfos.get(i);
                }
            }
        }
        if(length == 0 || length < 0){
            System.out.println("....");
        }
        return hostInfos.get(ThreadLocalRandom.current().nextInt(length));
    }


}
