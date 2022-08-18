package com.example.mybatisspring.dao;


import com.example.mybatisspring.annotation.WSelect;

public interface UserMapper {

    @WSelect("select 1")
    String selectByUserId(Integer userId);
}