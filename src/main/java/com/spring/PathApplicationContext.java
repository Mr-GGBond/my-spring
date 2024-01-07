package com.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

/**
 * @author sgw
 * @date 2024/01/07 12:12
 **/
public class PathApplicationContext {
    //配置类
    private Class configClass;

    private Map<String,BeanDefinition> beanDefinitionMap = new HashMap<>();

    private Map<String, Object> singletonObjects = new HashMap<>();

    private static final String singletonDesc = "singleton";

    public PathApplicationContext(Class configClass){
        //扫描
        scan(configClass);
        //构建单例池
        beanDefinitionMap.forEach((k,v) -> {
            if (v.getScope().equals("singleton")) {
                Object bean = createBean(k,v);
                singletonObjects.put(k,bean);
            }
        });
    }

    /**
     * 创建bean
     * @param beanName
     * @param beanDefinition
     * @return
     */
    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();
        Object instance = null;
        try {
            //创建bean的生命周期
            //推断构造方法
            instance = clazz.getConstructor().newInstance();
            //依赖注入

            //初始化前 AfterPropertiesSet

            //初始化 InitializingBean

            //初始化后 AOP

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 获取bean
     * @param beanName
     * @return
     */
    public Object getBean(String beanName){
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null){
            throw new RuntimeException("cannot found bean with name:" + beanName);
        }

        if (beanDefinition.getScope().equals(singletonDesc)) {
            Object singletonBean = singletonObjects.get(beanName);
            if (singletonBean == null) {
                singletonBean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, singletonBean);
            }
            return singletonBean;
        }else {
            Object prototypeBean = createBean(beanName,beanDefinition);
            return prototypeBean;
        }
    }

    /**
     * 扫描配置类，构建bean容器
     * @param configClass
     */
    private void scan(Class configClass) {
        //扫描配置文件，找到需要构建的bean
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan annotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String configPath = annotation.value();
            System.out.println(configPath);
            //解析path得到path下的文件

            String basePath = configPath.replace(".", "/");
            System.out.println(basePath);

            ClassLoader classLoader = PathApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(basePath);
            System.out.println(resource);

            //扫描文件夹下面的文件
            File file = new File(resource.getFile());
            System.out.println(file);
            //得到所有类
            if (file.isDirectory()){
                List<Class> clazzs = new ArrayList<>();
                clazzs = getScanClass(file, classLoader, clazzs);

                for (Class clazz : clazzs) {
                    Component componentAnno = (Component) clazz.getAnnotation(Component.class);
                    String beanName = componentAnno.value();
                    if ("".equals(beanName)){
                        String simpleName = clazz.getSimpleName();
                        System.out.println(simpleName);
                        beanName = Introspector.decapitalize(simpleName);
                        System.out.println(beanName);
                    }

                    //构建beanDefinition
                    BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setType(clazz);
                    beanDefinition.setScope(getScope(clazz));
                    beanDefinition.setLazy(getLazy(clazz));
                    beanDefinitionMap.put(beanName,beanDefinition);
                }
            }
        }
    }

    /**
     * 是否懒加载
     * @param clazz
     * @return
     */
    private Boolean getLazy(Class clazz) {
        if (clazz.isAnnotationPresent(Lazy.class)) {
            Lazy lazy = (Lazy) clazz.getAnnotation(Lazy.class);
            return lazy.value();
        }
        return false;
    }

    /**
     * 获取模式 单例or原型
     * @param clazz
     * @return
     */
    private String getScope(Class clazz) {
        String singleton = "singleton";
        if (clazz.isAnnotationPresent(Scope.class)) {
            Scope anno = (Scope) clazz.getAnnotation(Scope.class);
            String value = anno.value();
            if ("".equals(value)){
                value =  singleton;
            }
            return value;
        }else {
            return singleton;
        }
    }

    /**
     * 获取扫描包下面的所有需要bean容器管理的类
     * @param file
     * @param classLoader
     * @param clazzs
     * @return
     */
    private List<Class> getScanClass(File file, ClassLoader classLoader, List<Class> clazzs) {
        for (File f : file.listFiles()) {
            if (f.isDirectory()){
                getScanClass(f,classLoader, clazzs);
            }

            String absolutePath = f.getAbsolutePath();
            System.out.println(absolutePath);
            //使用类加载器和路径加载
            absolutePath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));
            System.out.println(absolutePath);
            absolutePath = absolutePath.replace("/", ".");
            System.out.println(absolutePath);

            try {
                Class<?> clazz = classLoader.loadClass(absolutePath);
                if (clazz.isAnnotationPresent(Component.class)) {
                    clazzs.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return clazzs;
    }
}
