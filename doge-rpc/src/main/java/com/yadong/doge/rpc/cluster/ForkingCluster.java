package com.yadong.doge.rpc.cluster;

import com.yadong.doge.rpc.invoker.InvokedResult;
import com.yadong.doge.rpc.invoker.Invoker;

/**
* @author YadongTan
* @date 2022/8/31 21:47
* @Description 并行调用多个服务提供者: 通过线程池创建多个线程, 并发调用多个provider, 结果保存到阻塞队列,
 *              只要有一个provider成功返回结果, 就会立即返回结果
*/
public class ForkingCluster extends AbstractCluster{
    @Override
    public Object clusterInvoke(Invoker invoker) {
        return null;
    }
}
