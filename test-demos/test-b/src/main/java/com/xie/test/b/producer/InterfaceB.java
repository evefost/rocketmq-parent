package com.xie.test.b.producer;


import com.xie.test.b.beans.User;
import com.xie.message.client.annotation.Producer;
import com.xie.message.client.annotation.Topic;

@Producer
//@Topic("TopicB")
public interface InterfaceB {

//    String noTag2(User user);
//    @Tag(value = "testA")
//    void testA(String a);
//
//    @Tag(value = "testB")
    @Topic("TopicB")
    String testB(User a);

}
