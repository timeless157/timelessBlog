package com.timeless.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author timeless
 * @create 2022-12-14 2:02
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SystemLog {
    String businessName();

}
