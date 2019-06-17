package com.xie.test.a.producer;


import com.xie.message.client.annotation.Producer;
import com.xie.message.client.annotation.Tag;
import com.xie.message.client.annotation.Topic;
import com.xie.message.client.annotation.TransMsg;
import com.xie.test.a.beans.User;

@Producer //标识有生产者接口
@Topic("TopicAJia-111-0304") //指定主题
public interface InterFaceA {
    /**
     * 注接口方法仅支持单个参数
     * @param user
     * @return
     */
//    @Topic("TopicA2")//将覆盖上面的Topic
    @Tag(value = "addUser") //非事务
    @TransMsg
    String addUser(User user);

    @Tag(value = "addUser2")
    @TransMsg //标识为有事务
    void addUser2(User user);

    @Tag(value = "addUser3")
    @TransMsg //标识为有事务
    void addUser3(User user);


    @Tag(value = "addUserNoTrans") //非事务
    String addUserNoTrans(User user);

    @Topic(value = "TopicA") //非事务
    String aaa(User user);


}
