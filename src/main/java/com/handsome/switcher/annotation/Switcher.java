package com.handsome.switcher.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface Switcher {
    /**
     * 开关状态，默认是关
     * @return
     */
    boolean state() default false;

    /**
     * 开关过期时间，时间单位是天
     * @return
     */
    int expiry() default 0;

    /**
     * 开关生效时间，时间单位是天
     * @return
     */
    int effective() default 0;
}
