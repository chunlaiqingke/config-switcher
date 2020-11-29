package com.handsome.switcher.aop;

import com.handsome.ConfigSwitcherLogger;
import com.handsome.apollo.ApolloSwitcherProperties;
import com.handsome.switcher.SwitcherDefinition;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.StringUtils;

/**
 * 完成开关方法代替的切面
 */
@Aspect
public class SwitcherAspect {
    @Autowired
    private AbstractApplicationContext applicationContext;

    @Around("execution(* com.handsome.*.*(..)) || @annotation(com.handsome.switcher.annotation.Switcher)")
    public Object execut(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getTarget().getClass().getSimpleName();
            //首字母改为小写
            String bdName = lower(className);
            //检查应用是否启动
            if(!applicationContext.isRunning()){
                return joinPoint.proceed();
            }
            //从qconfig中拿取结果
            SwitcherDefinition configSwitcher = ApolloSwitcherProperties.getConfigSwitcher(bdName + "." + methodName);
            //todo 过期时间和生效时间
            if(configSwitcher != null){
                return configSwitcher.isState();
            }
            ConfigSwitcherLogger.errorLog("没有此方法的开关配置，方法名：" + bdName + "." + methodName, new RuntimeException("no this method config"));
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        }
    }

    private String lower(String className) {
        if(StringUtils.isEmpty(className)){
            return className;
        }
        return className.substring(0,1).toLowerCase()+className.substring(1);
    }
}
