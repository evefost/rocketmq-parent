package com.xie.message.client.util;

import com.xie.message.client.pojo.MessageWrapper;

/**
 * @ClassName RetryStrategyUtil
 * @Author jiaguofang
 * @CreateAt 3/8/2019 14:26
 * @Version 1.0.0
 */
public final class RetryStrategyUtil {

    //TODO 这两个配置项 可配置化
    /* 发送 失败的RedisBlockingQueue最大容量 */
    public static final int FAILED_MSG_QUEUE_SIZE = 100000;

    /* 发送/消费 失败的RedisSet 最大容量 */
    public static final int MAX_MSG_SET_SIZE_IN_REDIS = 100000;
//    public static final int MAX_MSG_SET_SIZE_IN_REDIS = 10;

    /**
     * 即项目 xhg-rocketmq-retry-micro 中配置的InstanceName
     */
    public static final String INSTANCE_NAME_PLACEHOLDER = "PH";

    /**
     * key/value 分隔符
     */
    public static final String SEPARATOR_DOUBLE_COLON = "::";

    /**
     * 消息发送失败redis集合中的处于"重发处理中"状态的key
     */
    public static final String failSentMsgInProgressSetKeyPrefix = "rmq-fail-sent-msg-inprogress" + SEPARATOR_DOUBLE_COLON;

    /**
     * 消息发送失败的redisSet key
     */
    public static final String firstLevelKeySendFailPrefix = "rmq-msg-sent-fail" + SEPARATOR_DOUBLE_COLON;

    /**
     * 消息重试后依旧发送失败的redisSet key
     */
    public static final String firstLevelKeySendFailOverPrefix = "rmq-msg-sent-fail-over" + SEPARATOR_DOUBLE_COLON;

    /**
     * 消息消费失败的redisSet key
     */
    @Deprecated
    public static final String firstLevelKeyConsumeFailPrefix = "rmq-msg-consume-fail" + SEPARATOR_DOUBLE_COLON;

    /**
     * 消息发送失败,需要告警的redisSet key
     */
    public static final String sendFailReportKeyPrefix = "rmq-msg-sent-fail-report" + SEPARATOR_DOUBLE_COLON;

    /**
     * 消息消费失败,需要告警的redisSet key
     */
    public static final String consumeFailReportKeyPrefix = "rmq-msg-consume-fail-report" + SEPARATOR_DOUBLE_COLON;

    public static String getFirstLevelKeySendFail(MessageWrapper messageWrapper) {
        return getFirstLevelKeySendFail(messageWrapper.getSysType());
    }

    public static String getFailSentMsgInProgressSetKey(MessageWrapper messageWrapper) {
        return getFailSentMsgInProgressSetKey(messageWrapper.getSysType());
    }

    public static String getFirstLevelKeyFailOver(MessageWrapper messageWrapper) {
        return getFirstLevelKeyFailOver(messageWrapper.getSysType());
    }

    public static String getReportKeyFailSend(MessageWrapper messageWrapper) {
        return getReportKeyFailSend(messageWrapper.getSysType());
    }

    public static String getReportKeyFailConsume(MessageWrapper messageWrapper) {
        return getReportKeyFailConsume(messageWrapper.getSysType());
    }

    public static String getFirstLevelKeySendFail(String instanceName) {
        return RetryStrategyUtil.firstLevelKeySendFailPrefix + instanceName;
    }

    public static String getFailSentMsgInProgressSetKey(String instanceName) {
        return RetryStrategyUtil.failSentMsgInProgressSetKeyPrefix + instanceName;
    }

    public static String getFirstLevelKeyFailOver(String instanceName) {
        return RetryStrategyUtil.firstLevelKeySendFailOverPrefix + instanceName;
    }

    public static String getReportKeyFailSend(String instanceName) {
        return RetryStrategyUtil.sendFailReportKeyPrefix + instanceName;
    }

    public static String getReportKeyFailConsume(String instanceName) {
        return RetryStrategyUtil.consumeFailReportKeyPrefix + instanceName;
    }

    /**
     * 格式: TOPIC::TAG::MSG_ID::SCORE
     */
    public static String buildReportRedisValue(MessageWrapper messageWrapper, long score) {
        String value = messageWrapper.getTopic() + SEPARATOR_DOUBLE_COLON + messageWrapper.getTag()
            + SEPARATOR_DOUBLE_COLON + messageWrapper.getMsgId() + SEPARATOR_DOUBLE_COLON + score;
        boolean verboseMode = true;
//        verboseMode = false;
        if (verboseMode) {
            value += SEPARATOR_DOUBLE_COLON + new java.util.Date();
        }
        return value;
    }

    @Deprecated
    public static String getFirstLevelKeyConsumeFail(MessageWrapper messageWrapper) {
        return getFirstLevelKeyConsumeFail(messageWrapper.getSysType());
    }

    @Deprecated
    public static String getFirstLevelKeyConsumeFail(String instanceName) {
        return RetryStrategyUtil.firstLevelKeyConsumeFailPrefix + instanceName;
    }
}
