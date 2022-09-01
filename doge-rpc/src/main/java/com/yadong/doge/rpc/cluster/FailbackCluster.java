package com.yadong.doge.rpc.cluster;

import com.yadong.doge.rpc.invoker.InvokedResult;
import com.yadong.doge.rpc.invoker.Invoker;

/**
* @author YadongTan
* @date 2022/8/31 21:44
* @Description 失败重新恢复, 在调用失败, 记录日志和调用信息, 然后返回空结果给consumer,
 *             并且通过定时任务每隔5秒对失败的调用进行重试
*/
public class FailbackCluster extends AbstractCluster{

    @Override
    public Object clusterInvoke(Invoker invoker) {
        return null;
    }
}
