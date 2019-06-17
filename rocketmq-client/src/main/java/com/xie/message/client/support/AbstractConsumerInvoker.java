package com.xie.message.client.support;

import com.xie.message.client.pojo.MessageWrapper;
import com.xie.message.client.support.scan.ConsumerInvoker;
import com.xie.message.client.support.scan.MethodInfo;
import com.xie.message.client.support.scan.TopicPointInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 消息者自动调用
 * @author xie
 */
public abstract class AbstractConsumerInvoker<T> implements ApplicationContextAware ,InitializingBean{

    protected final Logger logger = LoggerFactory.getLogger(ConsumerInvoker.class);

    @Autowired
    protected TopicPointInfo consumerInfo;

    protected ApplicationContext applicationContext;

    public abstract T invoke(MessageWrapper messageWrapper) throws InvocationTargetException, IllegalAccessException;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, MethodInfo> methodInfoMap = consumerInfo.getTopicMethodMappings();
        if(methodInfoMap != null && methodInfoMap.size()>0){
            methodInfoMap.forEach((topic, methodInfo) -> {
                Object targetBean = applicationContext.getBean(methodInfo.getTargetClass());
                methodInfo.setTarget(targetBean);
            });
        }
    }
}
