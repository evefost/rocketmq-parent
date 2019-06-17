package com.xie.test.a.controller;

import com.xie.message.client.pojo.SendMsgResult;
import com.xie.message.client.pojo.SourceEvent;
import com.xie.message.client.support.MessagePublisher;
import com.xie.message.client.support.SendMsgCallback;
import com.xie.test.a.beans.User;
import java.io.IOException;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test2")
public class TestController implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    ApplicationContext applicationContext;

    @Autowired
    MessagePublisher publisher;

    @RequestMapping(value = "/onlyonceAsyncWithSize", method = {RequestMethod.GET, RequestMethod.POST})
    public String onlyonceAsyncWithSize(String topicName, String tag, Integer msgSize)
        throws InterruptedException, RemotingException, MQClientException, MQBrokerException, IOException {
        Assert.notNull(topicName, "param topicName can't be null");
//        Assert.notNull(msgSize, "param msgSize can't be null");
        User user = new User();
        user.setAge(99);
        user.setName("it's just a test message.");
        SourceEvent<User> sourceEvent = new SourceEvent(user);
        sourceEvent.setTopic(topicName);
        sourceEvent.setData(user);
        sourceEvent.setEventId(UUID.randomUUID().toString());
        if (StringUtils.isNotEmpty(tag)) {
            sourceEvent.setTag(tag);
        }
        this.publisher.publishEvent(sourceEvent, new SendMsgCallback() {
            @Override
            public void onSuccess(SendMsgResult sendResult) {
                logger.info("send is succ, sourceEvent:{} , sendResult:{}", sourceEvent, sendResult);
                logger.info("2 --- second");
            }

            @Override
            public void onException(Throwable e) {
                logger.info("send is fail , sourceEvent:" + sourceEvent, e);
                logger.info("3 --- third");
            }
        });
        return "succ";
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @RequestMapping(value = "/testexception", method = RequestMethod.GET)
    public String testexception(String topicname) throws Exception {
        logger.info("testexception -------------------------------->> ");
        throw new Exception("Vjia testexception");

    }
}