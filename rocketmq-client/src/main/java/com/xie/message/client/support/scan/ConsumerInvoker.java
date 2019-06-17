package com.xie.message.client.support.scan;

import com.alibaba.fastjson.JSON;
import com.xie.message.client.pojo.MessageWrapper;
import com.xie.message.client.support.IConsumerInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 消息者自动调用
 * @author xie
 */
public class ConsumerInvoker extends IConsumerInvoker<Boolean> {

    protected final Logger logger = LoggerFactory.getLogger(ConsumerInvoker.class);


    @Override
    public Boolean invoke(MessageWrapper messageWrapper) throws InvocationTargetException, IllegalAccessException {
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


}
