package com.sgw.service;

import com.spring.BeanInterface;
import com.spring.Component;
import com.spring.InitializingBean;

/**
 * 动物
 *
 * @author sgw
 * @date 2024/01/07 17:27
 **/
@Component
public class AnimalService implements InitializingBean {
    @Override
    public void afterPropertiesSet() {
        System.out.println("InitializingBean");
    }
}
