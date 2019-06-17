package com.xie.message.client.support;

import com.xie.message.client.DelayLevel;
import com.xie.message.client.pojo.MessageWrapper;
import com.xie.message.client.pojo.SendMsgResult;
import com.xie.message.client.pojo.SendStatus;
import com.xie.message.client.pojo.SourceEvent;
import org.apache.rocketmq.client.producer.SendResult;


public class GeneralMessagePublisher extends AbsMessagePublisher {

    private InnerMessageSender messageSender;


    public GeneralMessagePublisher(InnerMessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    protected void doSend(SourceEvent sourceEvent, DelayLevel delayLevel) {
        logger.debug("MQ发送非事务消息: Topic-Tag:[{}:{}]", sourceEvent.getTopic(), sourceEvent.getTag());
        MessageWrapper wrapper = new MessageWrapper(sourceEvent);
        wrapper.setDelayLevel(delayLevel);
        messageSender.sendAsycMessage(wrapper);
    }

    @Override
    public void publishEvent(SourceEvent sourceEvent, DelayLevel delayLevel) {
        doBeforePublish(sourceEvent);
        doSend(sourceEvent, delayLevel);
        doAfterPublish(sourceEvent);
    }

    @Override
    public SendMsgResult publishSycEvent(SourceEvent sourceEvent) {

        try {
            doBeforePublish(sourceEvent);
            MessageWrapper wapper = new MessageWrapper(sourceEvent);
            SendResult sendResult = messageSender.sendMessage(wapper);
            return convertResult(sendResult);
        } catch (Exception e) {
            logger.error(String.format("MQ发送同步消息失败: Topic-Tag:[%s:%s]", sourceEvent.getTopic(), sourceEvent.getTag()), e);
            throw new RuntimeException(String.format("发送同步消息失败: Topic-Tag:[%s:%s]", sourceEvent.getTopic(), sourceEvent.getTag()));
        } finally {
            doAfterPublish(sourceEvent);
        }
    }

    @Override
    public void publishEvent(SourceEvent sourceEvent, SendMsgCallback callback) {
        try {
            doBeforePublish(sourceEvent);
            MessageWrapper wapper = new MessageWrapper(sourceEvent);
            messageSender.sendAsycMessage(wapper, callback);
        } catch (Exception e) {
            logger.error(String.format("MQ发送异步消息失败: Topic-Tag:[%s:%s]", sourceEvent.getTopic(), sourceEvent.getTag()), e);
            throw new RuntimeException(String.format("发送异步消息失败: Topic-Tag:[%s:%s]", sourceEvent.getTopic(), sourceEvent.getTag()));
        } finally {
            doAfterPublish(sourceEvent);
        }
    }


    @Override
    public void publishTransEvent(SourceEvent sourceEvent) {
        throw new UnsupportedOperationException("不支持事务消息");
    }


    public static final SendMsgResult convertResult(SendResult sendResult) {
        SendMsgResult msgResult = new SendMsgResult();
        msgResult.setMsgId(msgResult.getMsgId());
        msgResult.setTransactionId(msgResult.getTransactionId());
        switch (sendResult.getSendStatus()) {
            case SEND_OK:
                msgResult.setStatus(SendStatus.SEND_OK);
                break;
            case FLUSH_DISK_TIMEOUT:
                msgResult.setStatus(SendStatus.FLUSH_DISK_TIMEOUT);
                break;
            case FLUSH_SLAVE_TIMEOUT:
                msgResult.setStatus(SendStatus.FLUSH_SLAVE_TIMEOUT);
                break;
            case SLAVE_NOT_AVAILABLE:
                msgResult.setStatus(SendStatus.SLAVE_NOT_AVAILABLE);
                break;
        }
        return msgResult;
    }


}
