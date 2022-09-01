package com.yadong.doge.rpc.invoker;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yadong.doge.utils.NameGenerateUtils;
import com.yadong.doge.utils.ObjectMapperUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public Invoker(Class<?> targetClass, Method method, Object[] args) {
        this.method = method;
        key = NameGenerateUtils.generateMethodMapKey(method, targetClass);
        this.args = args;
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
        return new InvokedResult(className, ObjectMapperUtils.toJSON(result));
    }


}
