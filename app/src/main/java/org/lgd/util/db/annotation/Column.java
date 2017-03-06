package org.lgd.util.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库工具类列名注解
 * Created by BruceWu on 2016/4/3.
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String column() default  "";       //列名
    String defaultValue() default "";  //默认值
}
