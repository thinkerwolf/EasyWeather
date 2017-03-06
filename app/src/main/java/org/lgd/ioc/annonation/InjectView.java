package org.lgd.ioc.annonation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>title:视图绑定注解</p>
 * <p>contentDescription:视图绑定注解</p>
 * Created by wukai on 2016/3/29.
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.LOCAL_VARIABLE})
public @interface InjectView {
    int value();
}
