package com.xie.message.client.support.scan;

import com.alibaba.fastjson.JSON;
import com.xie.message.client.pojo.SourceEvent;
import com.xie.message.client.support.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 生产者消息处理
 *
 * @author xieyang
 * @date 18/7/14
 */
public class ProducerInvocationHandler implements InvocationHandler {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private MessagePublisher publisher;

    private TopicPointInfo producerInfo;

    private ApplicationContext applicationContext;

    private String envPrefix;

    public ProducerInvocationHandler(TopicPointInfo producerInfo, ApplicationContext applicationContext) {
        this.producerInfo = producerInfo;
        this.applicationContext = applicationContext;
        this.publisher = applicationContext.getBean(MessagePublisher.class);
        this.envPrefix= producerInfo.getEnvPrefix();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("equals".equals(method.getName())) {
            try {
                Object
                        otherHandler =
                        args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                return equals(otherHandler);
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else if ("hashCode".equals(method.getName())) {
            return method.hashCode();
        } else if ("toString".equals(method.getName())) {
            return method.toString();
        }

        MethodInfo methodInfo = producerInfo.getMethodInfo(method);
        String key = methodInfo.getTopic() + ":" + methodInfo.getTag();
        logger.debug("发布消息 Topic-Tag:[{}{}], 方法参数: {}",key, JSON.toJSONString(args));
        SourceEvent sourceEvent = new SourceEvent(this);
        sourceEvent.setTopic(methodInfo.getTopic());
        sourceEvent.setTag(methodInfo.getTag());
        sourceEvent.setTrans(methodInfo.isTrans());
        if (args != null && args.length == 1) {
            sourceEvent.setData(args[0]);
            if (sourceEvent.isTrans()) {
                publisher.publishTransEvent(sourceEvent);
            } else {
                publisher.publishEvent(sourceEvent);
            }
        } else {
            throw new RuntimeException("parameters are not support");
        }
        Class<?> returnType = method.getReturnType();
        if (void.class == returnType) {
            return null;
        }
        return null;

    }
}
