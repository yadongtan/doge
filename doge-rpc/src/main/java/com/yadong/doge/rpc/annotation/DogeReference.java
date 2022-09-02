package com.yadong.doge.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
* @author YadongTan
* @date 2022/8/29 19:58
* @Description 标注在需要远程调用的类成员接口上
*/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DogeReference {
    String cluster() default "failover";    //默认集群容错策略  全小写
    String loadBalance() default "random";  //默认负载均衡策略 全小写
    String version() default "1.0"; //默认调用版本
}
