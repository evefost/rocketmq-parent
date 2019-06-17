package com.xie.message.client.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 消费者注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface Consumer {
}
