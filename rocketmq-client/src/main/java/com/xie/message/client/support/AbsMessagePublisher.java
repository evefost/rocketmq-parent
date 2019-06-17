package com.xie.message.client.support;

import com.xie.message.client.DelayLevel;
import com.xie.message.client.config.RocketmqProperties;
import com.xie.message.client.pojo.SourceEvent;
import com.xie.message.client.support.scan.TopicPointInfo;
import com.xie.message.client.util.ObjectId;
import com.xie.message.client.support.scan.TopicDeclareBeanRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


public abstract class AbsMessagePublisher implements MessagePublisher {

    protected final Logger logger = LoggerFactory.getLogger(AbsMessagePublisher.class);

    @Resource(name = TopicDeclareBeanRegistrar.PRODUCER_INFO)
    protected TopicPointInfo producerInfo;

    @Autowired
    protected RocketmqProperties rocketmqProperties;

    @Override
    public final void publishEvent(SourceEvent sourceEvent) {
        doBeforePublish(sourceEvent);
        doSend(sourceEvent, null);
        doAfterPublish(sourceEvent);
    }

    protected abstract void doSend(SourceEvent sourceEvent, DelayLevel delayLevel);

    protected void doBeforePublish(SourceEvent sourceEvent) {
        if (StringUtils.isEmpty(sourceEvent.getEventId())) {
            // UUID.randomUUID().toString();
            String msgId = ObjectId.get().toString();
            sourceEvent.setEventId(msgId);
        }
        if (StringUtils.isEmpty(sourceEvent.getTopic())) {
            //设置默认主题
            sourceEvent.setTopic(rocketmqProperties.getTopic());
        }
    }

    protected void doAfterPublish(SourceEvent sourceEvent) {

    }


}
