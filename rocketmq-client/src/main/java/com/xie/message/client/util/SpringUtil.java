package com.xie.message.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author jiaguofang
 * @version 1.0.0
 * @classname SpringUtil
 * @description TODO
 * @created 3/12/2019 11:26
 */
@Component
public final class SpringUtil implements ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(SpringUtil.class);
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext != null) {
            this.setAppContext(applicationContext);
        }
    }

    private static void setAppContext(ApplicationContext applicationContext) {
        SpringUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取Bean
     * @param beanName
     * @return
     */
    public static Object getBeanByName(String beanName) {
        return applicationContext.getBean(beanName);
    }

    /**
     * 通过class获取Bean
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBeanByClass(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 通过name以及class返回指定的Bean
     * @param beanName
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBeanByNameAndClass(String beanName, Class<T> clazz) {
        return applicationContext.getBean(beanName, clazz);
    }
}
