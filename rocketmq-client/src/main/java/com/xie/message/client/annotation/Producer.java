package com.xie.message.client.annotation;

import java.lang.annotation.*;

/**
 * 生产者注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Producer {
}
