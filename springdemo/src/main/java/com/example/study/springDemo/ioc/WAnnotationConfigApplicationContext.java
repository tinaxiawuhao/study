package com.example.study.springDemo.ioc;

import com.example.study.springDemo.annotation.Wautowired;
import com.example.study.springDemo.annotation.WcomponentScan;
import com.example.study.springDemo.annotation.Wscope;
import com.example.study.springDemo.annotation.Wservice;
import com.example.study.springDemo.config.BeanDefinition;
import com.example.study.springDemo.interfaceDemo.BeanNameAware;
import com.example.study.springDemo.interfaceDemo.BeanPostProcessor;
import com.example.study.springDemo.interfaceDemo.InitializingBean;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WAnnotationConfigApplicationContext {

    private ConcurrentHashMap<Class<?>, Object> singletonObjects = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Class<?>, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    // BeanPostProcessorList
    private ConcurrentHashMap<Class<?>, Object> beanPostProcessorMap = new ConcurrentHashMap<>();
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public WAnnotationConfigApplicationContext(Class<?> config) {
        // 扫描bean,并且得到所有beanDefinition放在beanDefinitionMap里
        scan(config);
        beanDefinitionMap.forEach((key, beanDefinition) -> {
            if ("singleton".equals(beanDefinition.Scope) && Objects.isNull(singletonObjects.get(key))) {
                Object bean = createBean(beanDefinition);
                singletonObjects.put(key, bean);
            }
        });


    }

    private Object createBean(BeanDefinition beanDefinition) {
        try {
            //初始化对象
            Object bean = beanDefinition.calzz.getDeclaredConstructor().newInstance();
            //处理对象属性
            Field[] declaredFields = bean.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(Wautowired.class)) {
                    Class<?> type = declaredField.getType();
                    Object fieldBean = getBean(type);

                    Class<?> next = null;
                    if (fieldBean == null) {
                        Set<Class<?>> resultSet = getClassBySuperOrInterface(type);
                        // 从全部的实现类中，选择一个实例去赋值给变量(比如有俩实现类，我根据名字或者随机挑出来一个去赋值)
                        next = resultSet.iterator().next();
                        fieldBean = getBean(next); // 现在是拿的第一个，可以循环遍历，再根据名称去匹配
                    }
                    if (fieldBean == null) {
                        fieldBean = createBean(beanDefinitionMap.get(next));
                    }
//                    Object o = getBean(declaredField.getName());
                    declaredField.setAccessible(true);
                    declaredField.set(bean, fieldBean);
                }
            }
            // 第三步：Aware回调以及初始化方法
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName();
            }

            // 第四步：BeanPostProcessor的前置方法
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                bean = beanPostProcessor.postProcessBeforeInitialization(bean);

            }
            // 第五步：初始化方法
            if (bean instanceof InitializingBean) {
                ((InitializingBean) bean).afterPropertiesSet();
            }
            // 第六步：BeanPostProcessor的后置方法
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                bean = beanPostProcessor.postProcessAfterInitialization(bean);
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void scan(Class<?> config) {
        //获取@WcomponentScan的扫描路径
        WcomponentScan wcomponentScan = config.getDeclaredAnnotation(WcomponentScan.class);
        //获取类加载器
        ClassLoader classLoader = WAnnotationConfigApplicationContext.class.getClassLoader();
        //获取扫描的包路径
        URL resource = classLoader.getResource(wcomponentScan.basePackage().replace(".", "/"));
//        System.out.println(resource);
        // 得到包下的文件
        assert resource != null;
        File file = new File(resource.getFile());
        //遍历循环包下文件
        scanFile(file);


    }

    private void scanFile(File file) {
        if (file.isDirectory()) {
            //获取包下所有文件
            File[] files = file.listFiles();
            assert files != null;
            for (File f : files) {
                scanFile(f);
            }
        } else {
            String absolutePath = file.getAbsolutePath();
            String classPath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class")).replace("\\", ".");
            System.out.println(classPath);
            //获取类加载器
            ClassLoader classLoader = WAnnotationConfigApplicationContext.class.getClassLoader();
            Class<?> aClass = null;
            try {
                aClass = classLoader.loadClass(classPath);
            } catch (Exception e) {
                e.printStackTrace();
            }

            assert aClass != null;
            if (aClass.isAnnotationPresent(Wservice.class)) {
                //扫描@Comptent注解发现有实现BeanPostProcessor的类
//                if (BeanPostProcessor.class.isAssignableFrom(aClass)) {
//                    // 就将此类通过反射创建出来
//                    BeanPostProcessor beanPostProcessor = (BeanPostProcessor) aClass.getDeclaredConstructor().newInstance();
//                    // 将这个后置处理器加到list里
//                    beanPostProcessorList.add(beanPostProcessor);
//                }
                //获取类上注解信息
//                Wservice declaredAnnotation = aClass.getDeclaredAnnotation(Wservice.class);
                //描述类创建
                BeanDefinition beanDefinition = new BeanDefinition();
                beanDefinition.setCalzz(aClass);
                if (aClass.isAnnotationPresent(Wscope.class)) {
                    Wscope wscope = aClass.getDeclaredAnnotation(Wscope.class);
                    beanDefinition.setScope(wscope.value());
                }
                //保存类信息
                beanDefinitionMap.put(aClass, beanDefinition);
            }

        }

    }

    private Set<Class<?>> getClassBySuperOrInterface(Class<?> superOrInterface) {
        // 获取到ioc容器中的所有key，
        Set<Class<?>> classes = beanDefinitionMap.keySet();
        // 准备一个返回结果
        Set<Class<?>> result = new HashSet<Class<?>>();

        for (Class<?> clazz : classes) {
            // superOrInterface.isAssignableFrom(clazz); clazz是superOrInterface的子类或者实现类时，返回true
            if (superOrInterface.isAssignableFrom(clazz) && !clazz.equals(superOrInterface)) { // 是其实现类，且不是他本身
                result.add(clazz);
            }
        }
        return result;
    }

    public Object getBean(Class<?> clazz) {
        if (beanDefinitionMap.containsKey(clazz)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(clazz);
            if ("singleton".equals(beanDefinition.Scope)) {
                return singletonObjects.get(clazz);
            } else {
                return createBean(beanDefinition);
            }
        }
        return null;
    }
}
