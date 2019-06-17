package com.xie.test.a.consumer;

import com.alibaba.fastjson.JSON;
import com.xie.message.client.pojo.TargetEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by dengyunhui on 2018/7/16.
 * <p>
 * 设备上报事件监听
 */
@Slf4j
@Component
public class DeviceEventListener {



    @EventListener(condition = "${event.topic}=='xhg-device-service-topic' && ${event.tag}=='DeviceEvent'")
    public void onMessage(TargetEvent event) throws IOException {
        log.info("设备上报事件监听#收到消息：{}", JSON.toJSONString(event));
    }
}
