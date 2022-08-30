package com.yadong.doge.utils;






import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class NameGenerateUtils {

    private static Logger logger = LoggerFactory.getLogger(NameGenerateUtils.class);

    //对象非接口类型
    public static String generateMethodMapKey(Method method, Object obj){
        StringBuilder builder = new StringBuilder();
        // 只传入接口类型, 接口方法
        // 返回 父接口类名#方法名#arg1.arg2.arg3 -- 方法
        String interfaceName;
        Class<?>[] interfaces = obj.getClass().getInterfaces();
        if(interfaces.length != 0){
            interfaceName = interfaces[0].getName();
        }else{
            interfaceName = obj.getClass().getName();
        }
        builder.append(interfaceName).append("#");
        builder.append(method.getName()).append("#");
        for (Parameter parameter : method.getParameters()) {
            builder.append(parameter.getName()).append(".");
        }
        logger.info("生成key:" + builder.toString());
        return builder.toString();
    }

    public static String generateZkNodePath(Method method, Object object){
        StringBuilder builder = new StringBuilder();
        // 只传入接口类型, 接口方法
        // 返回 父接口类名#方法名#arg1.arg2.arg3 -- 方法
        String interfaceName;
        Class<?>[] interfaces = object.getClass().getInterfaces();
        if(interfaces.length != 0){
            interfaceName = interfaces[0].getName();
        }else{
            interfaceName = object.getClass().getName();
        }
        builder.append("/").append(interfaceName).append("/");
        builder.append(method.getName());
        logger.info("生成key:" + builder.toString());
        return builder.toString();
    }

    public static String generateRedisPath(Method method, Object object){
        return NameGenerateUtils.generateZkNodePath(method, object);
    }

    public static String generateMethodMapKey(Method method, String interfaceName){
        StringBuilder builder = new StringBuilder();
        // 只传入接口类型, 接口方法
        // 返回 父接口类名#方法名#arg1.arg2.arg3 -- 方法
        builder.append(interfaceName).append("#");
        builder.append(method.getName()).append("#");
        for (Parameter parameter : method.getParameters()) {
            builder.append(parameter.getName()).append(".");
        }
        logger.info("生成key:" + builder.toString());
        return builder.toString();
    }
}
