package com.xie.test.b.consumer.impl;//package com.xhg.test.a.consumer.impl;

import com.xie.message.client.annotation.Consumer;
import com.xie.message.client.annotation.Tag;
import com.xie.message.client.annotation.Topic;
import com.xie.test.b.beans.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Consumer
@Topic("TopicC")
public class ServiceCImpl {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Tag("testC")
    public void testA(User a) {
        logger.info("消费端  serverC imple被调用了" + a);
    }
}
