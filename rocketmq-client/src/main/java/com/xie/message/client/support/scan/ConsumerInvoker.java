package com.xie.message.client.support.scan;

import com.alibaba.fastjson.JSON;
import com.xie.message.client.pojo.MessageWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 消息者自动调用
 * @author xie
 */
public class ConsumerInvoker implements ApplicationContextAware ,InitializingBean{

    protected final Logger logger = LoggerFactory.getLogger(ConsumerInvoker.class);

    @Autowired
    private TopicPointInfo consumerInfo;

    private ApplicationContext applicationContext;

    public boolean invoke(MessageWrapper messageWrapper) throws InvocationTargetException, IllegalAccessException {
        if (consumerInfo == null) {
            logger.warn("not scan consumer info");
            return false;
        }
        String key = StringUtils.isEmpty(messageWrapper.getTag()) ? messageWrapper.getTopic() : messageWrapper.getTopic() + ":" + messageWrapper.getTag();
        MethodInfo methodInfo = consumerInfo.getMethodInfo(key);
        if (methodInfo != null) {
            logger.debug("MsgKey(Topic-Tag) [{}] 自动匹配到业务代码", key);
            Object targetBean = methodInfo.getTarget();
            Method targetMethod = methodInfo.getMethod();
            Class<?>[] parameterTypes = targetMethod.getParameterTypes();
            Object arg0 = JSON.parseObject(messageWrapper.getData(), parameterTypes[0]);
            Object[] args = new Object[]{arg0};
            targetMethod.invoke(targetBean, args);
            return true;
        } else {
            logger.warn("MsgKey-MsgId [{}:{}] 未自动匹配到业务代码(检查消息配置的Topic/Tag)", key,messageWrapper.getMsgId());
            return false;
        }
    }

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
