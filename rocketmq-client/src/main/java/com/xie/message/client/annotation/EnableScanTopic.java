package com.xie.message.client.annotation;

import com.xie.message.client.support.scan.TopicDeclareBeanRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(TopicDeclareBeanRegistrar.class)
public @interface EnableScanTopic {

    /**
     * 生产接口包
     * @return
     */
    String[] producerPackages() default {};

    /**
     * 消费接口包
     * @return
     */
    String[] consumerPackages() default {};
}
