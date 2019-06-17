package com.xie.test.a.buz;

//import com.xhg.message.client.DelayLevel;
import com.xie.message.client.pojo.SourceEvent;
import com.xie.message.client.support.MessagePublisher;
import com.xie.message.client.util.IdGeneratorUtil;
import com.xie.test.a.beans.User;
import com.xie.test.a.producer.InterFaceA;
import com.xie.test.a.producer.InterfaceVjia;
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

    @Autowired(required = false)
    private InterfaceVjia vjTestNoTag007;

    int i = 0;

    public void addUser(int count) {
        User user = new User();
        user.setAge(count);
        user.setName("小黄狗资源再生"+ IdGeneratorUtil.getRandomString(10));
        interFaceA.addUser(user);
    }

    public void addUserNoTrans(int count) {
        User user = new User();
//        if(count % 2==0) {
//            int x = count/0;
//        }
        user.setAge(count);
        user.setName("小黄狗资源addUserNoTrans再生"+ IdGeneratorUtil.getRandomString(10));
        interFaceA.addUserNoTrans(user);
    }


    public void addUser2(int count) {
//        User user = new User();
//        user.setAge(count);
//        user.setName("小黄狗资源再生2");
//        interFaceA.addUser2(user);
        User user = new User();
        user.setAge(count);
        user.setName("publisher publishEvent发布的的消息");
        SourceEvent event = new SourceEvent(this,"TopicA","addUser2", user);
        publisher.publishTransEvent(event);
    }

    public void publishNoTransaction(int count) {
        User user = new User();
        user.setAge(count);
        user.setName("publisher publishEvent发布的的消息");
        SourceEvent event = new SourceEvent(this,"TopicA","addUser2", user);
//        publisher.publishEvent(event, DelayLevel.ONE_MINUTE);
    }

    public void publisherWithTransaction(int count) {
        User user = new User();
        user.setAge(count);
        user.setName("publisher publishTransEvent发布的的消息");
        SourceEvent event = new SourceEvent(this, "xhg-device-service-topic","DeviceEvent", user);
        publisher.publishTransEvent(event);
    }


    public void vjTestNoTag007(int count) {
        User user = new User();
        user.setAge(count);
        user.setName("sendVjiaUser:"+ IdGeneratorUtil.getRandomString(10));
        vjTestNoTag007.sendVjiaUser(user);
    }
}
