package com.sgw;

import com.sgw.service.TestService;
import com.spring.PathApplicationContext;

public class Main {
    public static void main(String[] args) {
        PathApplicationContext context = new PathApplicationContext(AppConfig.class);
        TestService testService = (TestService) context.getBean("testService");
        System.out.println(testService);
        System.out.println(context.getBean("testService"));
    }
}