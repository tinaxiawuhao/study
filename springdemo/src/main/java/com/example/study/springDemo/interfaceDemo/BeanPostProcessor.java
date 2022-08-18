package com.example.study.springDemo.interfaceDemo;

import com.sun.istack.internal.Nullable;

public interface BeanPostProcessor {
    @Nullable
    default Object postProcessBeforeInitialization(Object bean){
        return bean;
    }

    @Nullable
    default Object postProcessAfterInitialization(Object bean) {
        return bean;
    }
}
