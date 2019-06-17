package com.xie.message.client.support;

import com.alibaba.fastjson.JSON;
import com.xie.message.client.config.RocketmqProperties;
import com.xie.message.client.pojo.MessageWrapper;
import com.xie.message.client.pojo.TargetEvent;
import com.xie.message.client.support.scan.ConsumerInvoker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

@Slf4j
public class ConsumerMessageListener implements MessageListenerConcurrently {

    @Autowired
    protected RocketmqProperties properties;

    @Autowired
    protected ApplicationEventPublisher publisher;

    @Autowired(required = false)
    protected ConsumerInvoker invoker;


    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        MessageExt msg = msgs.get(0);
        MessageWrapper messageWrapper = null;
        try {
            log.info("收到非事务性消息:MsgId:[{}],Topic-Tags:[{}:{}]",msg.getMsgId(),msg.getTopic(),msg.getTags());
            messageWrapper = parserMessage(msg, MessageWrapper.class);
            return doAfterParseMessage(msg,messageWrapper);
        } catch (Throwable e) {
            Exception businessException = findBusinessException(e);
            if(businessException != null){
                log.warn("非事务性消息抛出业务异常(消费成功):MsgId:[{}],Topic-Tags:[{}:{}],ErrMsg:[{}]",
                    msg.getMsgId(),msg.getTopic(), msg.getTags(), businessException.getLocalizedMessage());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            if (msg.getReconsumeTimes() < properties.getConsumerRetryCount()) {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            log.error("非事务性消息消费失败:MsgId:[{}],Topic-Tags:[{}:{}],ReconsumeTimes:[{}],Properties.ConsumerRetryCount:[{}]",
                            msg.getMsgId(),msg.getTopic(),msg.getTags(), msg.getReconsumeTimes(), properties.getConsumerRetryCount(), e);
            if( null != messageWrapper) {
                messageWrapper.setConsumeErrTimes(msg.getReconsumeTimes());
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    protected ConsumeConcurrentlyStatus doAfterParseMessage(MessageExt msg,MessageWrapper messageWrapper) throws Exception {
        boolean autoInvoke = false;
        if(invoker != null){
            autoInvoke =invoker.invoke(messageWrapper);
        }
        if(!autoInvoke){
            this.publisher.publishEvent(new TargetEvent(msg,messageWrapper));
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    public final  <T extends MessageWrapper> T  parserMessage(MessageExt msg, Class<T> resultClazz) throws Exception {
        if (msg == null) {
            throw new RuntimeException("消息为空");
        }
        String msgContent = new String(msg.getBody(), "utf-8");
        if (StringUtils.isBlank(msgContent)) {
            throw new RuntimeException("消息为空");
        }
        T messageWrapper = JSON.parseObject(msgContent, resultClazz);
        messageWrapper.setMsgId(msg.getKeys());
        messageWrapper.setTopic(msg.getTopic());
        messageWrapper.setTag(msg.getTags());
        return messageWrapper;
    }

    protected RuntimeException findBusinessException(Throwable t) {
        if (t == null) {
            return null;
        }
        if (t instanceof RuntimeException) {
            return (RuntimeException) t;
        }
        return findBusinessException(t.getCause());
    }
}
