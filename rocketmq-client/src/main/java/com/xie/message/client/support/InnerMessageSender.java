package com.xie.message.client.support;

import com.xie.message.client.pojo.MessageWrapper;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.remoting.exception.RemotingException;

public interface InnerMessageSender {

    /**
     * test only
     */
    void resetCount();

    /**
     * test only
     */
    int getFailueCount();

    /**
     * test only
     */
    double getFailurePercentage();

    void sendAsycMessage(MessageWrapper messageWrapper);

    void sendAsycMessage(MessageWrapper messageWrapper, SendMsgCallback callback) throws RemotingException, MQClientException, InterruptedException;

    SendResult sendMessage(MessageWrapper messageWrapper) throws MQClientException, RemotingException, MQBrokerException, InterruptedException;
}
