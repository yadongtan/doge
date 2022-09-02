package com.yadong.doge.utils;






import com.sun.javafx.embed.HostInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class NameGenerateUtils {

    public static final String ZOOKEEPER_PATH_PREFIX = "/server";
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
        builder.append(method.getName());//.append("#");
//        for (Parameter parameter : method.getParameters()) {
//            builder.append(parameter.getName()).append(".");
//        }
        return generateMethodMapKey(method, interfaceName);
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
        return generateZkNodePath(method, interfaceName);
    }

    public static String generateZkNodePath(String methodName, String interfaceName){
        StringBuilder builder = new StringBuilder();
        // 只传入接口类型, 接口方法
        // 返回 父接口类名#方法名#arg1.arg2.arg3 -- 方法
        builder.append(ZOOKEEPER_PATH_PREFIX).append("/").append(interfaceName).append("/");
        builder.append(methodName);
        logger.info("生成ZkNodePath:" + builder.toString());
        return builder.toString();
    }

    public static String generateZkNodePath(Method method, String interfaceName){
        StringBuilder builder = new StringBuilder();
        // 只传入接口类型, 接口方法
        // 返回 父接口类名#方法名#arg1.arg2.arg3 -- 方法
        builder.append(ZOOKEEPER_PATH_PREFIX).append("/").append(interfaceName).append("/");
        builder.append(method.getName());
        logger.info("生成ZkNodePath:" + builder.toString());
        return builder.toString();
    }


    public static String generateZkNodePathWithHostInfo(Method method, String interfaceName){
        StringBuilder builder = new StringBuilder();
        // 只传入接口类型, 接口方法
        // 返回 父接口类名#方法名#arg1.arg2.arg3 -- 方法
        builder.append(ZOOKEEPER_PATH_PREFIX).append("/").append(interfaceName).append("/");
        builder.append(method.getName());
        logger.info("生成ZkNodePath:" + builder.toString());
        return builder.toString();
    }

    // 将zookeeper的节点路径还原为key
    // nodePath: /server/service.UserService/getUserInfo/192.168.2.100:20002
    // key: service.UserService#loginUser
    public static String generateKeyByZkNodePath(String nodePath){
        String[] nodes = nodePath.split("/");
        StringBuilder builder = new StringBuilder();
        if (nodes.length == 5 || nodes.length == 4) {
            builder.append(nodes[2]).append("#").append(nodes[3]);
            return builder.toString();
        }else{
            throw new UnsupportedOperationException();
        }
    }

    public static String generateZkNodePathByKey(String key){
        String[] names = key.split("#");
        return ZOOKEEPER_PATH_PREFIX + "/" + names[0] + "/" + names[1];
    }

    public static String getInterfaceNameFromKey(String key){
        return key.split("#")[0];
    }


    public static String getMethodNameFromKey(String key){
        return key.split("#")[1];
    }

    public static String generateRedisPath(Method method, Object object){
        return NameGenerateUtils.generateZkNodePath(method, object);
    }

    public static String generateMethodMapKey(Method method, String interfaceName){
        StringBuilder builder = new StringBuilder();
        // 只传入接口类型, 接口方法
        // 返回 父接口类名#方法名#arg1.arg2.arg3 -- 方法
        builder.append(interfaceName).append("#");
        builder.append(method.getName());//.append("#");
//        for (Parameter parameter : method.getParameters()) {
//            builder.append(parameter.getName()).append(".");
//        }
        logger.info("生成key:" + builder.toString());
        return builder.toString();
    }

    public static String generateMethodMapKey(Method method, Class<?> Class) {
        //如果是接口,
        if (Class.isInterface()) {
            return generateMethodMapKey(method, Class.getName());
        }else{
            return generateMethodMapKey(method, Class.getGenericInterfaces()[0]);
        }
    }

    public static String getHostAndPortFromZkNodePath(String path) {
        String[] split = path.split("/");
        return split[split.length - 1];
    }

}
