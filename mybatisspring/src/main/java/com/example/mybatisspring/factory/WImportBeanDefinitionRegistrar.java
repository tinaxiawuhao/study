package com.example.mybatisspring.factory;

import com.example.mybatisspring.annotation.WScan;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//ImportBeanDefinitionRegistrar的实现，通过获取@import上的注解找到mapper的扫描路径，通过classLoader加载磁盘下Class文件生成BeanDefinition并设置构造函数mapper类型参数，
// 最终将BeanDefinition注册到BeanDefinitionRegistry
public class WImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        //获取注解WScan对象，并取出value值作为扫描的路径
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(WScan.class.getName());
        assert annotationAttributes != null;
        //获取dao层扫描地址
        String mapperPath = annotationAttributes.get("value").toString();
        //遍历获取所有对象
        List<Class<?>> mappers = scan(mapperPath);
        for (Class<?> mapper : mappers) {
            //创建对于的beanDefinition注入到spring容器之中
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
            AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
            beanDefinition.setBeanClass(WFactoryBean.class);
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(mapper);
            registry.registerBeanDefinition(toLowerCaseFirstOne(mapper.getSimpleName()),beanDefinition);
        }
    }

    //通过路径遍历循环获取dao路径下面的类，进行加载
    private List<Class<?>> scan(String path) {
        List<Class<?>> classList = new ArrayList<>();
        path = path.replace(".", "/");
        //获取类加载器
        ClassLoader classLoader = WImportBeanDefinitionRegistrar.class.getClassLoader();
        URL url = classLoader.getResource(path);
        assert url != null;
        //获取包文件
        File file = new File(url.getFile());
        //遍历循环包下面所有类对象
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            assert files != null;
            for (File value : files) {
                String absolutePath = value.getAbsolutePath();
                absolutePath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));
                absolutePath = absolutePath.replace("\\", ".");
                try {
                    //加载对象
                    Class<?> aClass = classLoader.loadClass(absolutePath);
                    classList.add(aClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classList;
    }

    private String toLowerCaseFirstOne(String str){
        return str.substring(0, 1).toLowerCase().concat(str.substring(1));
    }
}