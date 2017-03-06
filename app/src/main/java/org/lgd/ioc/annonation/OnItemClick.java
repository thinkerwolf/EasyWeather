package org.lgd.ioc.annonation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * OnItemClick注解类
 * Created by Bruce Wu on 2016/3/30.
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@EventBase(listenerType = OnItemClick.class,listenerSetter = "setOnItemClickListener",methodName = "onItemClick")
public @interface OnItemClick {
    int value();
}
