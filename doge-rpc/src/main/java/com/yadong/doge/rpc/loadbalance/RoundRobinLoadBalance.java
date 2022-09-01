package com.yadong.doge.rpc.loadbalance;

import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.invoker.Invoker;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
* @author YadongTan
* @date 2022/9/1 11:49
* @Description 加权轮询
*/
public class RoundRobinLoadBalance extends AbstractLoadBalance{

    // 每一个节点保存一个
    // 一个invoker -- 多个节点 -- 每个节点对应一个权重信息
    private final ConcurrentHashMap<String,ConcurrentHashMap<String, WeightedRoundRobin>>
            methodWeightMap = new ConcurrentHashMap<>(128);

    private static final int RECYCLE_PERIOD = 60 * 1000;    // 移除非正常运行节点的时间间隔

    @Override
    public HostInfo doSelect(List<HostInfo> hostInfos, Invoker invoker) {
        String key = invoker.getKey();
        //从缓存中拿到WeightedRoundRobin
        ConcurrentHashMap<String, WeightedRoundRobin> map = methodWeightMap.computeIfAbsent(key, k -> new ConcurrentHashMap<>());
        int totalWeight = 0;
        long maxCurrent = Long.MIN_VALUE;
        long now = System.currentTimeMillis();  //当前时间
        HostInfo selectHostInfo = null;
        WeightedRoundRobin selectedWRR = null;
        for(HostInfo hostInfo : hostInfos){
            String identifyString = hostInfo.getHostAndPort();
            int weight = hostInfo.getHostData().getWeight();    //预热权重,维持一样, 懒得算了
            //从缓存中得到服务提供者对应的weightedRoundRobin,如果没有的话,new一个
            WeightedRoundRobin weightedRoundRobin = map.computeIfAbsent(identifyString, k -> {
                WeightedRoundRobin wrr = new WeightedRoundRobin();
                wrr.setWeight(weight);
                return wrr;
            });
            // 若缓存中的WeightedRoundRobin记录的固定权重和weight不一样,那么就更新weightedRoundRobin中的固定权重
            if(weight != weightedRoundRobin.getWeight()){
                // 更新权重
                weightedRoundRobin.setWeight(weight);
            }
            // weightedRoundRobin中的当前权重自增, 当前权重 = 当前权重 + 固定权重
            long cur = weightedRoundRobin.increaseCurrent();
            // 设置更新时间
            weightedRoundRobin.setLastUpdate(now);
            if(cur > maxCurrent){   //记录权重最大的一个节点
                maxCurrent = cur;
                selectHostInfo = hostInfo;
                selectedWRR = weightedRoundRobin;
            }
            totalWeight += weight;  //计算总权重
        }
        if(hostInfos.size() != map.size()){
            // 上一次更新的时间, 用来判断节点是否还存活。移除不存在的节点。
            map.entrySet().removeIf(item -> now - item.getValue().getLastUpdate() > RECYCLE_PERIOD);
        }
        if(selectHostInfo != null){
            selectedWRR.sel(totalWeight);   //当前权重 = 当前权重 - 总权重
            return selectHostInfo;  //返回权重最大的服务提供者节点
        }
        // 如果执行到这里, 说明有哪步出了问题
        return hostInfos.get(0);
    }

    protected static class WeightedRoundRobin{
        private int weight; //服务提供者固定权重
        private AtomicLong current = new AtomicLong(0); //当前权重
        private long lastUpdate;    //上一次更新的时间戳

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public AtomicLong getCurrent() {
            return current;
        }

        public void setCurrent(AtomicLong current) {
            this.current = current;
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }

        //当前权重自增, 当前权重 = 当前权重 + 固定权重
        public long increaseCurrent(){
            return current.addAndGet(weight);
        }

        public void sel(int weight){
            current.addAndGet((-weight));
        }
    }



}
