package com.xie.test.a.consumer.impl;

import com.alibaba.fastjson.JSON;
import com.xie.message.client.annotation.Consumer;
import com.xie.message.client.annotation.Tag;
import com.xie.message.client.annotation.Topic;
import com.xie.test.a.beans.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 注意，这些注解通常应标识在接口上，这里示例只是为了方便不写接口类直接标识实现类上
 */
@Service
@Consumer //这个标签一定要打上
@Topic(value = "TopicAJia-111-0304") //这里可标可不标识，但最终与方法组合必须有值
public class ServiceAImpl {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Tag(value = "addUser2")//最终结果topic与tag为：TopicA:addUser2
    String addUser(User user){
        logger.info("server a消费端  收到添加用户: "+ JSON.toJSONString(user));
       return "server a收到添加用户";
    }
//    @Topic(value = "TopicA2")//最终结果topic与tag为：TopicA2:addUser
    @Tag(value = "addUser")
    String addUser2(User user){
        logger.info("server a 消费端  收到添加用户2: "+ JSON.toJSONString(user));
        if(user.getAge()%2==0){
            throw new RuntimeException("xxxxxxx");
        }
        return "server a收到添加用户2";
    }
    @Tag(value = "addUserNoTrans")
    String addUserNoTrans(User user){
        logger.info("server a 消费端  收到添加用户2: "+ JSON.toJSONString(user));
        if(user.getAge()%2==0){
            throw new RuntimeException("xxxxxxx");
        }
        return "server a收到添加用户2";
    }

    @Topic(value = "TopicA2")//表示订阅topic为TopicA2(无tag)消息，同一topic全局仅可标识一个方法
    @Tag(value = "addUser3")
    String addUser3(User user){
        logger.info("server a 消费端  收到添加用户3: "+ JSON.toJSONString(user));

        return "server a收到添加用户3";
    }


    String addUser4(User user){
        logger.info("server a 消费端  收到添加用户2: "+ JSON.toJSONString(user));
        return "server a收到添加用户2";
    }
    //该方法不受影响（该方法不订阅任主题）
    String addUser5(User user){
        logger.info("server a 消费端  收到添加用户2: "+ JSON.toJSONString(user));
        return "server a收到添加用户2";
    }
}
