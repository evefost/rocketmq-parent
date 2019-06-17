package com.xie.message.client.support;

import com.xie.message.client.pojo.SendMsgResult;

/**
 * @author xie yang
 * @date 2018/8/27-15:45
 */
public interface SendMsgCallback {

    void onSuccess(final SendMsgResult sendResult);

    void onException(final Throwable e);
}
