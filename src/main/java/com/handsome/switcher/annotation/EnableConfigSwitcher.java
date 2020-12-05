package com.handsome.switcher.annotation;

import com.handsome.switcher.ConfigSwitcherBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动configswitcher,
 * 这是和springboot整合的关键，需要加在springboot的启动类上，在整合此jar中的类
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(ConfigSwitcherBeanDefinitionRegistrar.class)
public @interface EnableConfigSwitcher {
}
