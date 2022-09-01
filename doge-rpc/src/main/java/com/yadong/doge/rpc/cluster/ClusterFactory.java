package com.yadong.doge.rpc.cluster;

import com.yadong.doge.rpc.exception.UnknownClusterException;

public class ClusterFactory {

    public static Cluster getCluster(String clusterName){
        switch (clusterName) {
            case "failover":
                return new FailoverCluster();
            case "broadcast":
                return new BroadcastCluster();
            case "failback":
                return new FailbackCluster();
            case "failfast":
                return new FailfastCluster();
            case "failsafe":
                return new FailsafeCluster();
            case "forking":
                return new ForkingCluster();
            default:
                throw new UnknownClusterException("未知的Cluster:[" + clusterName + "]");
        }
    }
}
