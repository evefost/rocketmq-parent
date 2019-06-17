package com.xie.test.a.consumer.impl;

import com.xie.message.client.annotation.Consumer;
import com.xie.message.client.annotation.Topic;
import com.xie.test.a.beans.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Service
@Component
@Consumer //这个标签一定要打上
public class ServiceAImplVjia {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 20190408
     * @param user
     * @return
     */
    @Topic("InterfaceVjia")
//    @Topic("InterfaceVjiaaaaaa")
    public void  interfaceVjia(User user) {
        logger.info(" it's vjia  get user :{} ", user);
//        return "it's vjia . haha @ " + new java.util.Date();
    }
}
