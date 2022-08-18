package com.example.study.springTest.service;

import com.example.study.springDemo.annotation.Wautowired;
import com.example.study.springDemo.annotation.Wscope;
import com.example.study.springDemo.annotation.Wservice;

@Wservice
@Wscope
public class WhServiceImpl implements WhService{

    @Wautowired
    private WserviceTest wserviceTest;

    public void test() {
        System.out.println(wserviceTest);
    }
}
