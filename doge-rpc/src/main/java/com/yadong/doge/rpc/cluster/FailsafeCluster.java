package com.yadong.doge.rpc.cluster;

import com.yadong.doge.rpc.invoker.InvokedResult;
import com.yadong.doge.rpc.invoker.Invoker;

/**
* @author YadongTan
* @date 2022/8/31 21:46
* @Description 失败安全, 调用出现异常, 记录日志不抛出, 返回空结果
*/
public class FailsafeCluster extends AbstractCluster{
    @Override
    public Object clusterInvoke(Invoker invoker) {
        return null;
    }
}
