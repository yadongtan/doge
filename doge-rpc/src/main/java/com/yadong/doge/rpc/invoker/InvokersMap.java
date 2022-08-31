package com.yadong.doge.rpc.invoker;


import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NamedMethodGenerator;
import com.yadong.doge.utils.NameGenerateUtils;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
* @author YadongTan
* @date 2022/8/30 10:24
* @Description 收集对应的Invoker  , key为 类名 + 方法名 + 参数类型
*/
public class InvokersMap {

    HashMap<String, Invoker> map = new HashMap<>(128);

    public void put(Method method, Object obj) {
        String key = NameGenerateUtils.generateMethodMapKey(method, obj);
        Invoker invoker = new Invoker(method, obj);
        map.put(key, invoker);
    }


    public Invoker get(Method method, Object obj){
        return map.get(NameGenerateUtils.generateMethodMapKey(method, obj));
    }



}
