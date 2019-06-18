package com.xie.test.b.producer;


import com.xie.message.client.annotation.Producer;
import com.xie.message.client.annotation.Tag;
import com.xie.message.client.annotation.Topic;
import com.xie.message.client.annotation.TransMsg;
import com.xie.test.b.beans.User;

@Producer
@Topic("TopicA")
public interface InterFaceA {


    String noTag(User user);

    @Tag(value = "addUser")
    String addUser(User user);


    @Tag(value = "addUser2")
//    @TransMsg
    void addUser2(User user);
}
