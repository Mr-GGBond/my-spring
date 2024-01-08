package com.sgw;

import com.sgw.service.TestService;
import com.spring.BeanInterface;
import com.spring.PathApplicationContext;

public class Main {
    public static void main(String[] args) {
        PathApplicationContext context = new PathApplicationContext(AppConfig.class);
        BeanInterface testService = (BeanInterface) context.getBean("testService");
        System.out.println(testService);
        System.out.println(context.getBean("testService"));
        testService.test();
    }
}