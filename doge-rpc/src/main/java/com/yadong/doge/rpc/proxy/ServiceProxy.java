package com.yadong.doge.rpc.proxy;

import com.yadong.doge.rpc.annotation.DogeReference;
import com.yadong.doge.rpc.cluster.Cluster;
import com.yadong.doge.rpc.invoker.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
* @author YadongTan
* @date 2022/8/30 10:24
* @Description 为单个接口提供远程调用
*/
public class ServiceProxy {

    private static final Logger logger = LoggerFactory.getLogger(ServiceProxy.class);
    private Class<?> targetClass;
    private DogeReference dogeReference;

    private ServiceProxy(){

    }

    private ServiceProxy(Class<?> targetClass){
        this.targetClass = targetClass;
    }

    public DogeReference getDogeReference() {
        return dogeReference;
    }

    public void setDogeReference(DogeReference dogeReference) {
        this.dogeReference = dogeReference;
    }

    // 仅为需要远程调用的接口提供的代理创建方式
    public static ServiceProxy createServiceProxy(Class<?> targetClass, DogeReference dogeReference) {
        ServiceProxy serviceProxy = new ServiceProxy(targetClass);
        serviceProxy.setDogeReference(dogeReference);
        // 接口, jdk代理
//        if (targetClass.isInterface()) {
//            Object proxyInstance = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
//                    new Class[]{targetClass},
//                    new InvocationHandler() {
//                        @Override
//                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                            System.out.println("before...");
//                            // method.invoke(proxy, args); 错误写法, 这里的proxy代表代理类自身, 传入proxy会陷入无限循环
//                            Object result = serviceProxy.invoke(method, args, cluster);
//                            System.out.println("after...");
//                            return result;
//                        }
//                    });
//        }
        return serviceProxy;
    }

    //开始远程调用
    public Object invoke(Method method, Object[] args){
        Object result = null;
        Invoker invoker = new Invoker(targetClass, method, args, dogeReference);
        // TODO: 2022/8/31 还要要写一个Map, 存提供者的主机信息和对应的key, 也就是Directory的逻辑
        // TODO: 2022/8/31 这里就应该先去获取到服务提供者, 然后根据负载均衡策略选择一种方式调用, 发送invoker到服务提供者
        logger.info("开始远程调用...");
        try {
            result = invoker.invokeRemoted();
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }


}
