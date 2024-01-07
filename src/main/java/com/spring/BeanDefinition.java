package com.spring;

/**
 * @author sgw
 * @date 2024/01/07 13:34
 **/
public class BeanDefinition {
    //类型
    private Class type;
    //单例or原型
    private String scope;
    //是否懒加载
    private Boolean isLazy;

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Boolean getLazy() {
        return isLazy;
    }

    public void setLazy(Boolean lazy) {
        isLazy = lazy;
    }
}
