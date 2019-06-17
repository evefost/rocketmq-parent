package com.xie.message.client.pojo;

import com.alibaba.fastjson.JSON;
import com.xie.message.client.DelayLevel;
import com.xie.message.client.util.ObjectId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import static com.xie.message.client.pojo.MessageStatus.NOT_SEND;

/**
 * 消息包装类
 * @edited jgf
 */
public class MessageWrapper implements Serializable {

    /**     * 父级id,标识为同一次发起的事务     */
    protected String transactionId;

    protected String msgId;

    protected String topic;

    protected String tag;

    protected MessageStatus status=NOT_SEND;

    protected Integer sendErrTimes=0;

    /**     * json串     */
    protected String data;

    protected Date sendTime;

    protected String sendIp;

    protected String orderId;

    private transient DelayLevel delayLevel;

    /** 表示的延时时长(毫秒单位) */
    protected long scheduleSendTimeMillis;

    /** 此对象被更新的时间(毫秒) */
    protected long objectUpdateTimeMillis;

    /** 消费失败总次数 */
    protected Integer consumeErrTimes=0;

    /**   系统类型，例如配置中心 xhg-sys-config **/
    private String sysType;

    public MessageWrapper(){
        this.status = NOT_SEND;
        this.sendErrTimes = 0;
        this.sendTime = new Date();
        scheduleSendTimeMillis = 0;
        try {
            InetAddress addr = Inet4Address.getLocalHost();
            String ip = addr.getHostAddress().toString();
            this.sendIp = ip;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public MessageWrapper(String topic, String tag, String data){
        this.topic = topic;
        this.tag = tag;
        this.data = data;
        // UUID.randomUUID().toString();
        this.msgId = ObjectId.get().toString();
        this.status = NOT_SEND;
        this.sendErrTimes = 0;
        this.sendTime = new Date();
        scheduleSendTimeMillis = 0;
        try {
            InetAddress addr = Inet4Address.getLocalHost();
            String ip = addr.getHostAddress().toString();
            this.sendIp = ip;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public MessageWrapper(MessageWrapper messageWraper){
        this.transactionId = messageWraper.transactionId;
        this.msgId = messageWraper.msgId;
        this.topic = messageWraper.topic;
        this.tag = messageWraper.tag;
        this.status = messageWraper.status;
        this.data = messageWraper.data;
        this.sendErrTimes = messageWraper.sendErrTimes;
        this.sendTime = messageWraper.sendTime;
        this.sendIp = messageWraper.sendIp;
        this.orderId = messageWraper.getOrderId();
        scheduleSendTimeMillis = messageWraper.getScheduleSendTimeMillis();
        this.sysType = messageWraper.getSysType();
    }

    public MessageWrapper(SourceEvent sourceEvent){
        this.transactionId = sourceEvent.getTransactionId();
        this.msgId = sourceEvent.getEventId();
        this.topic = sourceEvent.getTopic();
        this.tag = sourceEvent.getTag();
        this.data = JSON.toJSONString(sourceEvent.getData());
        this.status = NOT_SEND;
        this.sendErrTimes = 0;
        this.sendTime = new Date();
        this.orderId = sourceEvent.getOrderId();
        scheduleSendTimeMillis = 0;
        try {
            InetAddress addr = Inet4Address.getLocalHost();
            String ip = addr.getHostAddress().toString();
            this.sendIp = ip;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public long getObjectUpdateTimeMillis() {
        return objectUpdateTimeMillis;
    }

    public void setObjectUpdateTimeMillis(long objectUpdateTimeMillis) {
        this.objectUpdateTimeMillis = objectUpdateTimeMillis;
    }

    public long getScheduleSendTimeMillis() {
        return scheduleSendTimeMillis;
    }

    public void setScheduleSendTimeMillis(long scheduleSendTimeMillis) {
        this.scheduleSendTimeMillis = scheduleSendTimeMillis;
    }

    public DelayLevel getDelayLevel() {
        return delayLevel;
    }

    public void setDelayLevel(DelayLevel delayLevel) {
        this.delayLevel = delayLevel;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getStatus() {
        return status.getValue();
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public String getMsgId() {
        return msgId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Integer getSendErrTimes() {
        return sendErrTimes;
    }

    public void setSendErrTimes(Integer sendErrTimes) {
        this.sendErrTimes = sendErrTimes;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendIp() {
        return sendIp;
    }

    public void setSendIp(String sendIp) {
        this.sendIp = sendIp;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getConsumeErrTimes() {
        return consumeErrTimes;
    }

    public void setConsumeErrTimes(Integer consumeErrTimes) {
        this.consumeErrTimes = consumeErrTimes;
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
