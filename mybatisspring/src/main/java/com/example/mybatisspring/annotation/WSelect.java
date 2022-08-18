package com.example.mybatisspring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //声明注解是方法上
@Retention(RetentionPolicy.RUNTIME)  //声明周期是运行时
public @interface WSelect {
    String value() default "";
}