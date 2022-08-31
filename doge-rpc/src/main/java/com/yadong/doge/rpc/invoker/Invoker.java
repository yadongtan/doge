package com.yadong.doge.rpc.invoker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yadong.doge.registry.client.zookeeper.ZookeeperRegistryClient;
import com.yadong.doge.utils.NameGenerateUtils;
import com.yadong.doge.utils.ObjectMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
* @author YadongTan
* @date 2022/8/30 10:24
* @Description 封装一个要调用方法, 包含本地调用和远程调用
*/
public class Invoker {

    @JsonIgnore
    transient private static final Logger logger = LoggerFactory.getLogger(Invoker.class);
    @JsonIgnore
    transient private Method method;
    @JsonIgnore
    transient private Object object;
    @JsonIgnore
    transient private Class<?> interfaceClass;
    private String invokeKey;
    private Object[] args;

    public Invoker(){

    }

    public void setInvokeKey(String invokeKey) {
        this.invokeKey = invokeKey;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getInvokeKey() {
        return invokeKey;
    }

    public void setInvokeKey(String invokeKey, Object[] args) {
        this.invokeKey = invokeKey;
        this.args = args;
    }

    public Invoker(Method method , Object object){
        this.method = method;
        this.object = object;
    }


    public Invoker(Method method, Class<?> interfaceClass, Object[] args){
        this.invokeKey = NameGenerateUtils.generateMethodMapKey(method, interfaceClass);
        this.method = method;
        this.interfaceClass = interfaceClass;
        this.args = args;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object invoke(){
        return null;
    }

    public static Logger getLogger() {
        return logger;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    //远程调用逻辑
    public Object remoteInvoke() {
        logger.info("[发起远程调用]: remoteInvoke()...");
        logger.info("[请求key]: " + NameGenerateUtils.generateMethodMapKey(method, interfaceClass));
        logger.info("[请求invoke序列化后结果]: " + ObjectMapperUtil.toJSON(this));
        return "success";
    }

    //本地调用逻辑
    public Object invoke(Object[] args){
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }



}
