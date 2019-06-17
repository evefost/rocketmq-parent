package com.xie.test.b.controller;

import com.alibaba.fastjson.JSON;
import com.xie.test.b.beans.User;
import com.xie.test.b.buz.ServiceAServiceImpl;
import com.xie.test.b.producer.InterFaceA;
import com.xie.test.b.producer.InterfaceB;
import com.xie.message.client.support.InnerMessageSender;
import com.xie.message.client.support.MessagePublisher;
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
@RequestMapping(value = "test")
public class TestController implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

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

    /**
     * 取样器结果：返回值报200，表示执行接口调试成功
     * 请求：发送的数据
     * 响应数据：返回的数据
     * Thread Name：线程组名称
     * Sample Start: 启动开始时间
     * Load time：加载时长
     * Latency：等待时长
     * Size in bytes：发送的数据总大小
     * Headers size in bytes：发送数据的其余部分大小
     * Sample Count：发送统计
     * Error Count：交互错误统计
     * Response code：返回码
     * Response message：返回信息
     * Response headers：返回的头部信息
     *
     * 所以，取消所有的异常捕获，而选择全部抛出，这样在JMeter中测试就可以获取到非200的相应代码，进而统计失败率。
     */
    @RequestMapping(value = "/onlyonceSync", method = RequestMethod.GET)
    public String onlyonceSync(String topicName) throws InterruptedException, RemotingException,
                                                    MQClientException, MQBrokerException, IOException {
        logger.info("61. topicName = {}", topicName);
        Assert.notNull(topicName, "The input TopicName can't be null !! ");
        User user = new User();
        user.setAge(99);
        user.setName("it's just a test message.");
        final String[] status = new String[1];
        String tags, keys;
        byte[] msgBody;
        tags = topicName;
        keys = topicName;
        msgBody = JSON.toJSONString(user).getBytes();
        Message message = new Message(topicName, tags, keys, msgBody);
        logger.info("1 --- first");
        SendResult sendResult = transactionMQProducer.send(message);
        SendStatus sendStatus = sendResult.getSendStatus();
        if(sendStatus.equals(SendStatus.SEND_OK)){
            logger.info("send is succ, sendResult:{}", sendResult);
            status[0] = "succ";
            logger.info("2 --- second");
        } else {
            logger.info("send is fail, sendResult:{}", sendResult);
            status[0] = "fail";
            logger.info("3 --- third");
            throw new IOException("Vjia SendStatus_fail_exception");
        }
        logger.info("4 --- i'm the last one.");
        return status[0];
    }

    private static final String ss = "A";
    private static final StringBuilder sb = new StringBuilder();
    static {
            int multiple = 20;
            for(int s=0;s<1024*multiple;s++){
                sb.append(ss);
            }
    }

    @RequestMapping(value = "/onlyonceSyncWithSize", method = {RequestMethod.GET, RequestMethod.POST})
    public String onlyonceSyncWithSize(String topicName, Integer msgSize) throws InterruptedException, RemotingException, MQClientException, MQBrokerException, IOException {
        Assert.notNull(topicName, "param topicName can't be null");
        Assert.notNull(msgSize, "param msgSize can't be null");
//        String ss = "A";
//        StringBuilder sb = new StringBuilder();
//        for(int s=0;s<msgSize;s++){
//            sb.append(ss);
//        }
        byte[] bytes = sb.toString().getBytes();
        Message msg = new Message(topicName, "tags", null, bytes);
        System.out.println(String.format("消息大小: %d, 消息:%s",bytes.length, sb));

        SendResult sendResult = transactionMQProducer.send(msg);
        SendStatus sendStatus = sendResult.getSendStatus();
        if(sendStatus.equals(SendStatus.SEND_OK)){
            logger.info("send is succ, sendResult:{}", sendResult);
            logger.info("2 --- second");
        } else {
            logger.info("send is fail, sendResult:{}", sendResult);
            logger.info("3 --- third");
            throw new IOException("Vjia SendStatus_fail_exception");
        }
        logger.info("4 --- i'm the last one.");
        return "succ";
    }

    @RequestMapping(value = "/{topicname}", method = RequestMethod.GET)
    public String pathTest(@PathVariable String topicname) {
        try {
            logger.info("61. topicname = {}", topicname);
            Assert.notNull(topicname, "The input TopicName can't be null !! ");
            User user = new User();
            user.setAge(99);
            user.setName("it's just a test message.");
            final String[] status = new String[1];
            String tags, keys;
            byte[] msgBody;
            tags = topicname;
            keys = topicname;
            msgBody = JSON.toJSONString(user).getBytes();
            Message message = new Message(topicname, tags, keys, msgBody);
            logger.info("1 --- first");
            transactionMQProducer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    logger.info("send is succ, sendResult:{}", sendResult);
                    status[0] = "succ";
                    logger.info("2 --- second");
                }

                @Override
                public void onException(Throwable e) {
                    logger.info("send is fail, throwable:{}", e);
                    status[0] = "fail";
                    logger.info("3 --- third");
                }
            });
            logger.info("4 --- i'm the last one.");
            return status[0];
        } catch (MQClientException e) {
            e.printStackTrace();
            logger.error("MQClientException occur", e);
        } catch (RemotingException e) {
            e.printStackTrace();
            logger.error("RemotingException occur", e);
        } catch (InterruptedException e) {
            logger.error("InterruptedException occur", e);
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    @RequestMapping(value = "/concurrentTest", method = {RequestMethod.GET, RequestMethod.POST})
    public String concurrentTest(String topicName, Integer threads,Integer msgCount, Integer msgSize) {
        System.out.println(String.format(" threads = %s, msgCount=%s", threads, msgCount));
        Assert.notNull(threads, "param threads can't be null");
        Assert.notNull(msgCount, "param msgCount can't be null");
        String ss = "A";
        StringBuilder sb = new StringBuilder();
        for(int s=0;s<msgSize;s++){
            sb.append(ss);
        }
        byte[] bytes = sb.toString().getBytes();
        Message msg = new Message(topicName, "tags", null, bytes);
        System.out.println(String.format("消息大小: %d, 消息:%s",bytes.length, sb));
        innerMessageSender.resetCount();
        count++;
        AtomicInteger msgtotal = new AtomicInteger(0);
        AtomicInteger msgFailTotal = new AtomicInteger(0);
        System.out.println(String.format("前端打开线程数：%d； 各发送消息量：%d",threads, msgCount));
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CountDownLatch finishCountDownLatch = new CountDownLatch(threads);

        for(int i=0;i<threads;i++){
            new Thread(){
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                        System.out.println("每个线程开始发送消息");
                        for(int j=0;j<msgCount;j++){
                            msgtotal.incrementAndGet();
//                            interFaceA.addUser(user);
                            SendResult sendResult=transactionMQProducer.send(msg);

                        }
                    } catch (InterruptedException | MQClientException | RemotingException | MQBrokerException e) {
//                        e.printStackTrace();
                        logger.error("147.msg.send.error.", e);
                        msgFailTotal.incrementAndGet();
                    } catch(Exception e) {
                        logger.error("150.exception", e);
                        msgFailTotal.incrementAndGet();
                    }
                    finishCountDownLatch.countDown();
                }
            }.start();
        }
        countDownLatch.countDown();
        try {
            finishCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("InterruptedException occur", e);
        }
        System.out.println("A消息发送完总数:"+msgtotal.intValue()+" 失败数: "+innerMessageSender.getFailueCount()
                                            +"   失败率:"+innerMessageSender.getFailurePercentage());
        logger.info("目标topic name ： {} ； 消息大小： {} ", topicName, msgSize);
        logger.info("B消息发送完总数:"+msgtotal.intValue()+" 失败数: "+msgFailTotal
                                            +"   失败率:"+(((double)msgFailTotal.get())*100/((double)msgtotal.get()))+"%");
        return "addUser2:"+count;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        String s="";
        int length = 1;
        String c = "a";
//        c="中";
        char ch='x';
        for(int i = 0; i<length; i++) {
            s = s + c;
        }
        System.out.println(String.format("s=%s, s.byte.length=%s", s, s.getBytes().length));
        System.out.println(String.format("c=%s, c.byte.length=%s", c, c.getBytes().length));
    }



    @RequestMapping(value = "/testexception", method = RequestMethod.GET)
    public String testexception(String topicname) throws Exception {
        logger.info("testexception -------------------------------->> ");
        throw new Exception("Vjia testexception");

    }
}