package com.yadong.doge.rpc.cluster;

import com.yadong.doge.rpc.invoker.InvokedResult;
import com.yadong.doge.rpc.invoker.Invoker;
import com.yadong.doge.rpc.invoker.InvokerAndResultMap;

/**
* @author YadongTan
* @date 2022/8/31 19:32
* @Description 给ServiceProxy调用, 负责从多个主机中
 * 根据容错策略,执行挑选Directory挑选主机
*/
public interface Cluster {

    Object clusterInvoke(Invoker invoker);


}
