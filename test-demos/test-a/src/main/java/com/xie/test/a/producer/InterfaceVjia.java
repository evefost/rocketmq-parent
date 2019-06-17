package com.xie.test.a.producer;

import com.xie.message.client.annotation.Producer;
import com.xie.message.client.annotation.Topic;
import com.xie.test.a.beans.User;

/**
 * @author jiaguofang
 * @version 1.0.0
 * @classname InterfaceVjia
 * @created 4/8/2019 11:10
 */
@Producer
public interface InterfaceVjia {

    @Topic("InterfaceVjia")
    String sendVjiaUser(User user);

}
