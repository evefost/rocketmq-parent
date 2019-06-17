package com.xie.test.a.consumer;

import com.xie.message.client.pojo.TargetEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsumerDemoListener {


    @EventListener(condition = "#event.topic=='xhg-device-service-topic' && #event.tag=='DeviceEvent'")
    public void rocketmqMsgListen(TargetEvent event) throws Exception {

        log.info("业务处理[TopicTestwgr:TagA] consumer 监听到一个消息达到：{}",event.getData());
    }



}
