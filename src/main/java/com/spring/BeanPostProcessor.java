package com.spring;

/**
 * @author sgw
 * @date 2024/01/07 18:05
 **/
public interface BeanPostProcessor {

    Object postProcessorBeforeInitialization(Object bean,String beanName);


    Object postProcessorAfterInitialization(Object bean,String beanName);
}
