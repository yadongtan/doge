package com.yadong.doge.rpc.cluster;

import com.yadong.doge.rpc.invoker.InvokedResult;
import com.yadong.doge.rpc.invoker.Invoker;

/**
* @author YadongTan
* @date 2022/8/31 21:43
* @Description 逐个调用每个provider, 如果其中一台报错, 在循环调用结束后, 抛出异常
*/
public class BroadcastCluster extends AbstractCluster{

    @Override
    public Object clusterInvoke(Invoker invoker) {
        return null;
    }
}
