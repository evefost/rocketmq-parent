package com.xie.message.client.support;

import com.alibaba.fastjson.JSON;
import com.xie.message.client.config.RocketmqProperties;
import com.xie.message.client.pojo.MessageWrapper;
import com.xie.message.client.pojo.SendMsgResult;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

public class MessageSender implements InnerMessageSender {

    private Logger logger = LoggerFactory.getLogger(MessageSender.class);

    private volatile AtomicInteger totalSendCount = new AtomicInteger(0);

    private volatile AtomicInteger failureCount = new AtomicInteger(0);

    @Autowired
    private RocketmqProperties rocketmqProperties;

    private DefaultMQProducer defaultProducer;


    public MessageSender(DefaultMQProducer defaultProducer) {
        this.defaultProducer = defaultProducer;
    }


    @Override
    public void resetCount() {
        failureCount.set(0);
        totalSendCount.set(0);
    }

    @Override
    public int getFailueCount() {
        return failureCount.get();
    }

    @Override
    public double getFailurePercentage() {
        return (double) failureCount.get() / (double) totalSendCount.get();
    }

    @Override
    public void sendAsycMessage(MessageWrapper messageWrapper) {
        sendAsycMessage(messageWrapper, null);

    }


    @Override
    public void sendAsycMessage(MessageWrapper messageWrapper, SendMsgCallback callback) {
        totalSendCount.incrementAndGet();
        Message msg = new Message(messageWrapper.getTopic(),
                messageWrapper.getTag(),
                messageWrapper.getMsgId(),
                JSON.toJSONString(messageWrapper).getBytes());
        if (messageWrapper.getDelayLevel() != null) {
            msg.setDelayTimeLevel(messageWrapper.getDelayLevel().getLevelValue());
        }
        try {
            if (messageWrapper.getOrderId() != null) {
                defaultProducer.send(msg,
                        (mqs, msg1, arg) -> {
                            String id = (String) arg;
                            int index = Math.abs(id.hashCode()) % mqs.size();
                            return mqs.get(index);
                        },
                        messageWrapper.getOrderId(),
                        new SendCallback() {
                            @Override
                            public void onSuccess(SendResult sendResult) {
                                logger.debug("RocketMq发送异步消息成功: MsgId:[{}],Topic:[{}]", sendResult.getMsgId(), messageWrapper.getTopic());
                                SendMsgResult msgResult = GeneralMessagePublisher.convertResult(sendResult);
                                if (callback != null) {
                                    callback.onSuccess(msgResult);
                                }

                            }

                            @Override
                            public void onException(Throwable e) {
                                failureCount.incrementAndGet();
                                logger.error("RocketMq发送非事务消息失败-(稍后将发起重试)-Topic:[{}],MsgId:[{}],Namesrv地址:[{}],SendMsgTimeout:[{}]",
                                        messageWrapper.getTopic(), messageWrapper.getMsgId(), rocketmqProperties.getServerAddr(), defaultProducer.getSendMsgTimeout(), e);
                                if (callback != null) {
                                    callback.onException(e);
                                }
                            }
                        });
            } else {
                defaultProducer.send(msg, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        logger.debug("RocketMq发送消息成功: MsgId:[{}]", sendResult.getMsgId());
                        SendMsgResult msgResult = GeneralMessagePublisher.convertResult(sendResult);
                        if (callback != null) {
                            callback.onSuccess(msgResult);
                        }

                    }

                    @Override
                    public void onException(Throwable e) {
                        logger.error("RocketMq发送消息失败-Topic:[{}],MsgId:[{}],Namesrv地址:[{}],SendMsgTimeout:[{}]",
                                messageWrapper.getTopic(), messageWrapper.getMsgId(), rocketmqProperties.getServerAddr(), defaultProducer.getSendMsgTimeout(), e);
                        failureCount.incrementAndGet();
                        if (callback != null) {
                            callback.onException(e);
                        }
                    }
                });
            }

        } catch (Exception e) {
            logger.error("消息失败,Message " + JSON.toJSONString(messageWrapper), e);
        }
    }

    @Override
    public SendResult sendMessage(MessageWrapper messageWrapper) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        Message msg = new Message(messageWrapper.getTopic(),
                messageWrapper.getTag(),
                messageWrapper.getMsgId(),
                JSON.toJSONString(messageWrapper).getBytes());
        totalSendCount.incrementAndGet();
        return defaultProducer.send(msg);
    }


}
