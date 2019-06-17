package com.xie.message.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

import static com.xie.message.client.config.RocketmqProperties.PREFIX;

/**
 * 配置了才启用
 * @author xieyang
 */
@Data
@ConfigurationProperties(PREFIX)
public class RocketmqProperties {

    public static final String PREFIX = "spring.extend.mq";

    public static final String DEFAULT_TOPIC = "default_topic";

    /**
     * mq nameserver 集群地址，用“;”分割地址
     */
    private String serverAddr;

    /**
     * 实例名称
     */
    private String instanceName;

    private String clientIP;


    /**
     * 生产者默认主题 default_topic，可设定
     */
    private String topic = DEFAULT_TOPIC;

    /**
     * 消费者订阅主题,格式:topic1:tag1,topic2:tag2
     */
    private List<String> subscribe;
    /**
     * 消息消费重试次数，从0开始
     */
    private int consumerRetryCount=8;
    /**
     * 消息发送重试次数，默认为8
     */
    private int sendRetryMaxTimes = 8;

    /**
     * 消息发送处理时的超时时间(超过此值则抛弃不再处理， 其值官方默认 3000ms;
     * 压测时设置为200000,否则会大量报错TooMuchRequest)
     */
    private int sendMsgTimeout = 5000;

}
