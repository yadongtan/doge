package com.yadong.doge.rpc.invoker;


import com.yadong.doge.utils.NameGenerateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
* @author YadongTan
* @date 2022/9/1 15:45
* @Description 服务提供者来找对应的方法进行调用并返回给消费者的map
*/
public class RpcMethodObjectMap {

    private static final Logger logger = LoggerFactory.getLogger(RpcMethodObjectMap.class);

    // 父接口类名 + 方法名 + 方法参数 -- 方法
    HashMap<String, Method> methodMap = new HashMap<>(256);
    // 父接口类名 + 方法名 + 方法参数 -- 对应的obj, 用来执行方法
    HashMap<String, Object> methodObjectMap = new HashMap<>(256);

    private static RpcMethodObjectMap _INSTANCE;

    private RpcMethodObjectMap(){

    }

    public static RpcMethodObjectMap getInstance() {
        if(_INSTANCE == null){
            synchronized (RpcMethodObjectMap.class){
                if(_INSTANCE == null){
                    _INSTANCE = new RpcMethodObjectMap();
                }
            }
        }
        return _INSTANCE;
    }

    private void addInvokerMethod(Class<?> targetClass, Method method, Object obj){
        String key = NameGenerateUtils.generateMethodMapKey(method, targetClass);
        addInvokerMethod(key, method, obj);
    }

    private void addInvokerMethod(String key, Method method, Object obj){
        methodMap.put(key, method);
        methodObjectMap.put(key, obj);
        logger.info("加入map:[" + key + " : " + " # " + method.toString() + " # " + obj.toString() + "]");
    }

    public Method getTargetMethod(String key){
        return methodMap.get(key);
    }

    public Object getTargetObject(String key){
        return methodObjectMap.get(key);
    }

    public void addInvokerMethod(Method method, Object obj){
        String key = NameGenerateUtils.generateMethodMapKey(method, obj);
        addInvokerMethod(key, method, obj);
    }

    public void addInvokerMethod(Object obj){
        String interfaceName = obj.getClass().getInterfaces()[0].toString();
        Method[] declaredMethods = obj.getClass().getDeclaredMethods();//拿到所有方法
        for (Method method : declaredMethods) { //每个方法都加入map
            addInvokerMethod(method, obj);
        }
    }

    public boolean isEmpty(){
        return methodMap.isEmpty();
    }
}
