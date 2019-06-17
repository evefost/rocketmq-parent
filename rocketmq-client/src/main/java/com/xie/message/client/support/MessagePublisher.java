package com.xie.message.client.support;

import com.xie.message.client.DelayLevel;
import com.xie.message.client.pojo.SendMsgResult;
import com.xie.message.client.pojo.SourceEvent;


public interface MessagePublisher {

    /**
     * 普通非事务性消息,异步的
     *
     * @param sourceEvent
     */
    void publishEvent(SourceEvent sourceEvent);

    /**
     * 异步发送延时消息
     * @param sourceEvent
     * @param delayLevel
     */
    void publishEvent(SourceEvent sourceEvent,DelayLevel delayLevel);

    /**
     * 事务性消，异步的
     *
     * @param sourceEvent
     */
    void publishTransEvent(SourceEvent sourceEvent);


    /**
     * 普通非事务性消息,同步的
     * @param sourceEvent
     */
    SendMsgResult publishSycEvent(SourceEvent sourceEvent);

    /**
     * 普通非事务性消息,异步带回调
     * @param sourceEvent
     */
    void publishEvent(SourceEvent sourceEvent,SendMsgCallback callback);

}
