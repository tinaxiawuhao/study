package com.example.mybatisspring.service;

import com.example.mybatisspring.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    UserMapper userMapper;

    public void getUser(){
        userMapper.selectByUserId(1);
    }
}