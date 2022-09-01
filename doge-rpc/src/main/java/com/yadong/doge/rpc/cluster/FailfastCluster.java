package com.yadong.doge.rpc.cluster;

import com.yadong.doge.rpc.invoker.InvokedResult;
import com.yadong.doge.rpc.invoker.Invoker;

/**
* @author YadongTan
* @date 2022/8/31 21:46
* @Description 快速失败, 只会调用一次, 失败后立即抛出异常
*/
public class FailfastCluster extends AbstractCluster{
    @Override
    public Object clusterInvoke(Invoker invoker) {
        return null;
    }
}
