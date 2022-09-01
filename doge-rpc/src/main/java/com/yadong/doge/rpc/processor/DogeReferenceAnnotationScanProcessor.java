package com.yadong.doge.rpc.processor;

import com.yadong.doge.rpc.annotation.DogeReference;
import com.yadong.doge.rpc.cluster.Cluster;
import com.yadong.doge.rpc.proxy.ServiceProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
* @author YadongTan
* @date 2022/8/31 12:25
* @Description 扫描所有标注了@DogeService的类,生成代理对象
*/
public class DogeReferenceAnnotationScanProcessor implements BeanPostProcessor{


    @Nullable
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            Class<?> type = field.getType();
            DogeReference dogeReference = field.getAnnotation(DogeReference.class);
            if (dogeReference != null) {
                ServiceProxy serviceProxy = ServiceProxy.createServiceProxy(field.getType(), dogeReference);
                //需要进行代理
                try {
                    Object proxyInstance = (Object) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{field.getType()}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            //System.out.println("开始远程调用...");
                            // method.invoke(proxy, args); 错误写法, 这里的proxy代表代理类自身, 传入proxy会陷入无限循环
                            // TODO: 2022/8/28 去远程调用执行返回获取返回值叭
                            return serviceProxy.invoke(method, args);
                        }
                    });
                    field.setAccessible(true);
                    field.set(bean, proxyInstance);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }


}
