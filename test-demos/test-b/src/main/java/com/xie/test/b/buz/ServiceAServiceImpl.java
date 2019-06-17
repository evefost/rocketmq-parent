package com.xie.test.b.buz;


import com.xie.test.b.beans.User;
import com.xie.test.b.producer.InterFaceA;
import com.xie.message.client.pojo.SourceEvent;
import com.xie.message.client.support.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceAServiceImpl {

    private Logger logger = LoggerFactory.getLogger(ServiceAServiceImpl.class);


    @Autowired
    private ServiceB serviceB;

    @Autowired
    private ServiceC serviceC;



    @Autowired(required = false)
    private MessagePublisher publisher;



    @Autowired(required = false)
    private InterFaceA interFaceA;

    int i = 0;
    public void noTag(int count) {
        User user = new User();
        user.setAge(count);
        user.setName("小黄狗资源再生noTag");
        interFaceA.noTag(user);
    }

    public void addUser(int count) {
        User user = new User();
        user.setAge(count);
        user.setName("小黄狗资源再生");
        interFaceA.addUser(user);
    }



    public void addUser2(int count) {
        User user = new User();
        user.setAge(count);
        user.setName("小黄狗资源再生2");
        interFaceA.addUser2(user);
    }


    public void addUser3(int count) {
        User user = new User();
        user.setAge(count);
        user.setName("小黄狗资源再生3,publish");
        SourceEvent event = new SourceEvent(this, "topic", user);
        publisher.publishEvent(event);

    }

    public void publishTopicC(int count) {
        User user = new User();
        user.setAge(count);
        user.setName("小黄狗资源再生3,publishTopicC ");
        SourceEvent event = new SourceEvent(this, "TopicC", user);
        publisher.publishEvent(event);

    }

    public void publishTopicCAddUser(int count) {
        User user = new User();
        user.setAge(count);
        user.setName("小黄狗资源再生3,publishTopicC addUser");
        SourceEvent event = new SourceEvent(this, "TopicC", "addUser",user);
        publisher.publishEvent(event);

    }
}
