package com.xie.message.client.pojo;

/**
 * @author xie yang
 * @date 2018/8/27-15:12
 */
public class SendMsgResult {

   private SendStatus status;

    private String msgId;

    private String transactionId;

    public SendStatus getStatus() {
        return status;
    }

    public void setStatus(SendStatus status) {
        this.status = status;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
