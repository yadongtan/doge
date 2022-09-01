package com.yadong.doge.rpc.invoker;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yadong.doge.rpc.annotation.DogeReference;
import com.yadong.doge.rpc.cluster.BroadcastCluster;
import com.yadong.doge.rpc.cluster.Cluster;
import com.yadong.doge.rpc.cluster.ClusterFactory;
import com.yadong.doge.rpc.cluster.FailoverCluster;
import com.yadong.doge.utils.NameGenerateUtils;
import com.yadong.doge.utils.ObjectMapperUtils;
import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YadongTan
 * @date 2022/8/28 21:50
 * @Description 用来封装一个方法调用的全部信息, 并通过netty传输
 */
public class Invoker {

    private String key;
    private Object[] args;
    @JsonIgnore
    private Method method;
    @JsonIgnore
    private String loadBalanceName;
    @JsonIgnore
    private String clusterName;
    @JsonIgnore
    private String interfaceName;
    @JsonIgnore
    private static AtomicInteger publicLockCount = new AtomicInteger(0);
    @JsonIgnore
    private Class<?> targetClass;

    private Integer lockId;

    public Invoker(){

    }
    public Invoker(Class<?> targetClass, Method method, Object[] args, DogeReference dogeReference) {
        this.method = method;
        key = NameGenerateUtils.generateMethodMapKey(method, targetClass);
        this.args = args;
        this.loadBalanceName = dogeReference.loadBalance();
        this.clusterName = dogeReference.cluster();
        this.interfaceName = targetClass.getName();
        this.lockId = publicLockCount.incrementAndGet();
        this.targetClass = targetClass;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public void setLockId(Integer lockId) {
        this.lockId = lockId;
    }

    public Integer getLockId() {
        return lockId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getLoadBalanceName() {
        return loadBalanceName;
    }

    public void setLoadBalanceName(String loadBalanceName) {
        this.loadBalanceName = loadBalanceName;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public static ExecutorService getExecutor() {
        return executor;
    }

    private static final ExecutorService executor = Executors.newCachedThreadPool();


    public Invoker(String key, Object[] args){
        this.key = key;
        this.args = args;
    }

    //在本地执行目标方法
    public InvokedResult invoke() throws InvocationTargetException, IllegalAccessException {
        Method targetMethod = RpcMethodObjectMap.getInstance().getTargetMethod(key);
        Object targetObject = RpcMethodObjectMap.getInstance().getTargetObject(key);
        Object result = targetMethod.invoke(targetObject, args);
        String className = result.getClass().getTypeName();
        return new InvokedResult(className, ObjectMapperUtils.toJSON(result), lockId);
    }

    //去远程调用目标方法
    public Object invokeRemoted() throws InvocationTargetException, IllegalAccessException {
        return ClusterFactory.getCluster(clusterName).clusterInvoke(this);
    }


}
