package com.example.study.springDemo.config;

import com.example.study.springDemo.annotation.WcomponentScan;
import com.example.study.springDemo.annotation.Wconfiguration;

@Wconfiguration
@WcomponentScan(basePackage="com.example.study.springTest")
public class ScanConfiguration {
}
