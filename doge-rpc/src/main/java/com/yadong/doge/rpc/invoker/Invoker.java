package com.yadong.doge.rpc.invoker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
* @author YadongTan
* @date 2022/8/30 10:24
* @Description 封装一个要调用方法, 包含本地调用和远程调用
*/
public class Invoker {

    private static final Logger logger = LoggerFactory.getLogger(Invoker.class);
    private Method method;
    private Object object;

    public Invoker(){

    }

    public Invoker(Method method ,Object object){
        this.method = method;
        this.object = object;
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
}
