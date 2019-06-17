package com.xie.test.a.buz;

import com.alibaba.fastjson.JSON;
import com.xie.message.client.annotation.Consumer;
import com.xie.message.client.annotation.Tag;
import com.xie.message.client.annotation.Topic;
import com.xie.message.client.support.MessagePublisher;
import com.xie.test.a.beans.User;
import com.xie.test.a.producer.InterFaceA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
@Consumer
@Topic(value = "TopicA")
public class ConsumerAServiceImpl {
    private Logger logger = LoggerFactory.getLogger(ConsumerAServiceImpl.class);

     @Autowired(required = false)
    private MessagePublisher publisher;

    @Autowired(required = false)
    private InterFaceA interFaceA;

    int i = 0;

    @Topic("TopicAJia-111ttt")
    @Tag("addUser")
    public String addUser(User user){
        logger.info("consumer消费端  收到添加用户 vjia: "+ JSON.toJSONString(user));
        int uid = user.getAge();
        if(uid%2==0) {
            int x = 9/0;
        }
        return "consumer 收到添加用户 vjia : " + JSON.toJSONString(user);
    }


}
