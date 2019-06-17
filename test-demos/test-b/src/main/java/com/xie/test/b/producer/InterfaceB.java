package com.xie.test.b.producer;


import com.xie.message.client.annotation.Tag;
import com.xie.test.b.beans.User;
import com.xie.message.client.annotation.Producer;
import com.xie.message.client.annotation.Topic;

@Producer
@Topic("TopicB")
public interface InterfaceB {


    @Tag("testB")
    String testB(User a);

}
