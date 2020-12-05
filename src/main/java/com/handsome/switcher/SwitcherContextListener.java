package com.handsome.switcher;

import com.handsome.apollo.ApolloSwitcherProperties;
import com.handsome.switcher.annotation.Switcher;
import com.handsome.utils.JacksonUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 这里是监听spring启动完成的消息，
 * 加载所有配置了switcher注解的bean，
 * 然后去config中创建配置
 */
public class SwitcherContextListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        AbstractApplicationContext applicationContext = (AbstractApplicationContext)event.getApplicationContext();
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        //apollo只支持单个配置的新增，所以这里只能循环去操作
        for (String bdName : beanDefinitionNames) {
            Class<?> type = applicationContext.getType(bdName);
            appendUpdate(bdName, findMethod(bdName, type, Switcher.class));
        }
    }

    /**
     * @param methodsWithAnnotation
     * @return
     */
    private void appendUpdate(String bdName, List<Method> methodsWithAnnotation){
        for (Method method : methodsWithAnnotation) {
            //这里要用spring的反射去拿，而不可以自己使用字节码去拿，拿不到
            Switcher switcherAnno = AnnotationUtils.findAnnotation(method, Switcher.class);
            SwitcherDefinition switcherDefinition = new SwitcherDefinition();
            switcherDefinition.setState(switcherAnno.state());
            switcherDefinition.setExpiry(switcherAnno.expiry());
            switcherDefinition.setEffective(switcherAnno.effective());
            ApolloSwitcherProperties.appendUpdate(bdName + "." + method.getName(), JacksonUtil.toJSONString(switcherDefinition));
        }
    }

    /**
     * qconfig中没有的就是新配置，需要append到qconfig中去的
     * @return
     */
    private boolean configContains(String bdName, Method method){
        return ApolloSwitcherProperties.containsKey(bdName + "." + method.getName());
    }

    /**
     * 获取需要加到config中的方法
     * jdk1.8+
     * @param clazz
     * @param annotationClazz
     * @return
     */
    private List<Method> findMethod(String bdName, Class<?> clazz, Class<? extends Annotation> annotationClazz){
        //继承的方法不算
        Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(m -> AnnotationUtils.findAnnotation(m, annotationClazz) != null && !configContains(bdName, m))
                .collect(Collectors.toList());
    }
}
