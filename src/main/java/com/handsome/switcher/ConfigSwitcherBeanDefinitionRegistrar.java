package com.handsome.switcher;

import com.handsome.apollo.ApolloSwitcherProperties;
import com.handsome.switcher.aop.SwitcherAspect;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 针对springboot开发
 */
public class ConfigSwitcherBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AbstractBeanDefinition switcherAspectBD = BeanDefinitionBuilder.genericBeanDefinition(SwitcherAspect.class).getBeanDefinition();
        beanDefinitionRegistry.registerBeanDefinition("switcherAspect", switcherAspectBD);
        AbstractBeanDefinition qConfigSwitcherProperties = BeanDefinitionBuilder.genericBeanDefinition(ApolloSwitcherProperties.class).getBeanDefinition();
        beanDefinitionRegistry.registerBeanDefinition("qConfigSwitcherProperties", qConfigSwitcherProperties);
        AbstractBeanDefinition switcherContextListenerBD = BeanDefinitionBuilder.genericBeanDefinition(SwitcherContextListener.class).getBeanDefinition();
        beanDefinitionRegistry.registerBeanDefinition("switcherContextListener", switcherContextListenerBD);
    }
}
