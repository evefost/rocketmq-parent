package com.xie.test.b.consumer.impl;

import com.alibaba.fastjson.JSON;
import com.xie.test.b.beans.User;
import com.xie.message.client.annotation.Consumer;
import com.xie.message.client.annotation.Tag;
import com.xie.message.client.annotation.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xieyang on 18/7/14.
 */
//@Service
@Consumer
@Topic(value = "TopicB")
public class
ServiceAImpl {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

//    @Tag(value = "testA")
//    void testA(String a){
//        logger.info("消费端  test 方法被执行:"+a);
//    }
//
//    @Tag(value = "testB")
//    void testB(String a,int age){
//        logger.info("消费端  test 方法被执行:"+a+"age"+age);
//    }
//
//    @Tag(value = "baseParams2")
//    String baseParams2(String description,int age,User user){
//        logger.info("消费端  serverAimpl 多参数 方法被执行: "+description+"--age "+age+"==user: "+ JSON.toJSONString(user));
//
//        return null;
//    }

    @Tag(value = "addUser2")
    String addUser(User user){
        logger.info("server b消费端  收到添加用户: "+ JSON.toJSONString(user));
       return "server b收到添加用户2";
    }
//    @Topic(value = "TopicA2")
    @Tag(value = "addUser")
    String addUser2(User user){
        logger.info("server a 消费端  收到添加用户2: "+ JSON.toJSONString(user));
        return "server b收到添加用户";
    }

    @Topic(value = "TopicC")
    String topicC(User user){
        logger.info("server a 消费端  收到添加用户NoTag: "+ JSON.toJSONString(user));
        return "server b收到添加用户NoTag";
    }
//    @Topic(value = "TopicC")
    String topicC2(User user){
        logger.info("server a 消费端  收到添加用户NoTag: "+ JSON.toJSONString(user));
        return "server b收到添加用户NoTag";
    }

    @Topic(value = "TopicC")
    @Tag(value = "addUser")
    String topicCAddUser(User user){
        logger.info("server a 消费端  收到添加用户 addUser: "+ JSON.toJSONString(user));
        if(true){
            throw  new RuntimeException("假状失败了");
        }

        return "server b收到添加用户NoTag";
    }

//    @Topic(value = "TopicA3")
//    @Tag(value = "addUser")
//    String topicA3AddUser(User user){
//        logger.info("TopicA3 a 消费端  收到添加用户 addUser: "+ JSON.toJSONString(user));
//        return "server b收到添加用户NoTag";
//    }
//
//    @Topic(value = "${testTop}")
//    String testTop(User user){
//        logger.info("server a 消费端  收到添加用户 addUser: "+ JSON.toJSONString(user));
//        return "server b收到添加用户NoTag";
//    }



    String NoTagssss(User user){
        logger.info("server a 消费端  xxxxxx: "+ JSON.toJSONString(user));
        return "server xxxxx";
    }
    @Topic(value = "pre_test")
    String topicCAddUserss(User user){
        logger.info("sdddddddddddddddddddddr: "+ JSON.toJSONString(user));
        return "server b收到添加用户NoTag";
    }

}
