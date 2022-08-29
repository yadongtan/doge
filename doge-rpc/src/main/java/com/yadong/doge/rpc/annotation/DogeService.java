package com.yadong.doge.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
* @author YadongTan
* @date 2022/8/29 19:57
* @Description 标注在类上, 表明此服务需要被暴露
*/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface DogeService {
}
