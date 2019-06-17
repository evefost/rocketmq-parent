package com.xie.message.client.support.scan;

import java.lang.reflect.Proxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * 生产bean处理
 */
class ProducerFactoryBean implements FactoryBean<Object>, InitializingBean, ApplicationContextAware, EnvironmentAware {

    private ApplicationContext applicationContext;

    protected Environment environment;

    private Class<?> type;

    private String name;

    private String topic;

    private TopicPointInfo producerInfo;


    @Override
    public Object getObject() throws Exception {
        Object proxy = Proxy.newProxyInstance(ProducerFactoryBean.class.getClassLoader(), new Class[]{type}, new ProducerInvocationHandler(producerInfo,applicationContext));
        return proxy;
    }



    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public TopicPointInfo getProducerInfo() {
        return producerInfo;
    }

    public void setProducerInfo(TopicPointInfo producerInfo) {
        this.producerInfo = producerInfo;
    }

    @Override
    public Class<?> getObjectType() {
        return this.type;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public String toString() {
        return new StringBuilder("MyTestFactoryBean{")
                .append("type=").append(type).append(", ")
                .append("name='").append(name).append("', ")
                .append("topic='").append(topic).append("', ")
                .append("}").toString();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
