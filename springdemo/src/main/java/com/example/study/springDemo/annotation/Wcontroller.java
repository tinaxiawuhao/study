package com.example.study.springDemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //声明注解是类上
@Retention(RetentionPolicy.RUNTIME)  //声明周期是运行时
public @interface Wcontroller {
}
