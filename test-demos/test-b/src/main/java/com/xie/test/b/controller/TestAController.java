package com.xie.test.b.controller;

import com.alibaba.fastjson.JSON;
import com.xie.message.client.support.InnerMessageSender;
import com.xie.message.client.support.MessagePublisher;
import com.xie.test.b.beans.User;
import com.xie.test.b.buz.ServiceAServiceImpl;
import com.xie.test.b.producer.InterFaceA;
import com.xie.test.b.producer.InterfaceB;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping(value = "testA")
public class TestAController {
    private static final Logger logger = LoggerFactory.getLogger(TestAController.class);

    @Autowired
    private ServiceAServiceImpl transactionService;

    ApplicationContext applicationContext;

    @Autowired(required = false)
    private InterFaceA interFaceA;

    @Autowired(required = false)
    private InterfaceB interfaceB;

    @Autowired(required = false)
    InnerMessageSender innerMessageSender;

    @Autowired(required = false)
    MessagePublisher publisher;

    @Autowired(required = false)
    DefaultMQProducer transactionMQProducer;

    int count = 0;


    @RequestMapping(value = "/addUser", method = RequestMethod.GET)
    public void addUser(String topicName) {
        interFaceA.addUser(new User());

    }

    @RequestMapping(value = "/addUser2", method = RequestMethod.GET)
    public void addUser2(String topicName) {
        interFaceA.addUser2(new User());

    }


    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public void create(String topic) throws MQClientException {
        transactionMQProducer.createTopic(null,topic,16);

    }

}