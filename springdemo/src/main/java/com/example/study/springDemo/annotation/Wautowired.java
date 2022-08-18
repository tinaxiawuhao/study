package com.example.study.springDemo.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD) //声明注解是字段上
//@Target(ElementType.TYPE) //声明注解是类上
@Retention(RetentionPolicy.RUNTIME)  //声明周期是运行时
public @interface Wautowired {
}
