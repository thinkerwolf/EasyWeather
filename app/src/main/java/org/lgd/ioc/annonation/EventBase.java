package org.lgd.ioc.annonation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 凯 on 2016/2/17.
 */
@Target({ElementType.TYPE})   //注解作用域
@Retention(RetentionPolicy.RUNTIME)              //注解生命周期
@Documented                                      //文档中可以展示
public @interface EventBase {
    Class<?> listenerType();
    String listenerSetter();
    String methodName();
}
