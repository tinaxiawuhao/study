package com.example.study;


import com.example.study.springDemo.config.ScanConfiguration;
import com.example.study.springDemo.ioc.WAnnotationConfigApplicationContext;
import com.example.study.springTest.service.WhServiceImpl;


public class SpringdempApplication {

    public static void main(String[] args) {
        WAnnotationConfigApplicationContext wAnnotationConfigApplicationContext= new WAnnotationConfigApplicationContext(ScanConfiguration.class);
        WhServiceImpl bean = (WhServiceImpl)wAnnotationConfigApplicationContext.getBean(WhServiceImpl.class);
        System.out.println(bean);
        bean.test();
    }

}
