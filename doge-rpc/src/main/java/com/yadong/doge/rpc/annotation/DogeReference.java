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

}
