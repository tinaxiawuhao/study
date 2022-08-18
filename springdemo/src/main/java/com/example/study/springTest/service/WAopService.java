package com.example.study.springTest.service;

import com.example.study.springDemo.interfaceDemo.BeanPostProcessor;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class WAopService implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean) {
        System.out.println("后置处理器，初始化后");
        if (bean.getClass() == WAopService.class) {
            return Proxy
                    .newProxyInstance(WAopService.class.getClassLoader(), bean.getClass().getInterfaces(),
                            (Object p, Method m, Object[] a) -> {
                                System.out.println("代理逻辑前");
                                Object c=m.invoke(bean,a);
                                System.out.println("代理逻辑后");
                                return c;
                            });
        }
        return bean;
    }
}
