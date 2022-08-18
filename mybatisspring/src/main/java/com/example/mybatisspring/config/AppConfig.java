package com.example.mybatisspring.config;

import com.example.mybatisspring.annotation.WScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.example.mybatisspring"})
@WScan("com.example.mybatisspring.dao")
public class AppConfig {
}