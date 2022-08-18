package com.example.mybatisspring.factory;

import com.example.mybatisspring.annotation.WSelect;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//FactoryBean实现,这里通过构造函数传入接口的Class类型，将类型通过Jdk动态代理生成并返回对象，getObject返回对象注册到spring容器中
// 当调用目标对象后会执行代理对象invoke方法，
// 从invoke方法通过反射与注解获取到sql语句，后续流程就可以利用Mybatis提供操作数据库流程
public class WFactoryBean implements FactoryBean {

    private final Class<?> mapper;

    public WFactoryBean(Class<?> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Object getObject() throws Exception {
        //使用动态代理机制
        return Proxy.newProxyInstance(WFactoryBean.class.getClassLoader(), new Class[]{mapper}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (Object.class.equals(method.getDeclaringClass())){
                    return method.invoke(this,args);
                }
                WSelect annotation = method.getAnnotation(WSelect.class);
                System.out.println("调用方法名称是"+method.getName()+",sql语句为"+annotation.value());
                //后面可执行Mybatic操作数据库的相关操作
                return null;
            }
        });
    }

    @Override
    public Class<?> getObjectType() {
        return mapper;
    }
}