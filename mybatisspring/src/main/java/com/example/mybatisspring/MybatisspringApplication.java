package com.example.mybatisspring;

import com.example.mybatisspring.config.AppConfig;
import com.example.mybatisspring.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MybatisspringApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = applicationContext.getBean("userService", UserService.class);
        userService.getUser();
    }

}
