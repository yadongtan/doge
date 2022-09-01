package com.yadong.doge.rpc.loadbalance;

import com.yadong.doge.rpc.exception.UnknownClusterException;
import com.yadong.doge.rpc.exception.UnknownLoadBalanceException;
import com.yadong.doge.rpc.invoker.Invoker;

public class LoadBalanceFactory {


    public static LoadBalance getLoadBalance(Invoker invoker) throws UnknownLoadBalanceException {
        String loadBalanceName = invoker.getLoadBalanceName();
        switch (loadBalanceName) {
            case "hash":
                return new ConsistentHashLoadBalance();
            case "leastActive":
                return new LeastActiveLoadBalance();
            case "random":
                return new RandomLoadBalance();
            case "round":
                return new RandomLoadBalance();
            case "shortestResponse":
                return new ShortestResponseLoadBalance();
            default:
                throw new UnknownLoadBalanceException("未知的LoadBalance:[" + loadBalanceName + "]");
        }
    }
}
