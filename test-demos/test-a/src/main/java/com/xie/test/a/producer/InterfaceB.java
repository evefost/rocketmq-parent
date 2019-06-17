package com.xie.test.a.producer;


import com.xie.message.client.annotation.Producer;
import com.xie.message.client.annotation.Tag;
import com.xie.message.client.annotation.Topic;
import com.xie.test.a.beans.User;

@Producer
@Topic("TopicB")
public interface InterfaceB {

//    @Tag(value = "testA")
//    void testA(String a);
//
    @Tag(value = "testB")
    String testB(User a);

}
