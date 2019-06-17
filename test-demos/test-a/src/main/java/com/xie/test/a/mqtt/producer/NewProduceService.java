package com.xie.test.a.mqtt.producer;

import com.xie.message.client.pojo.SendMsgResult;
import com.xie.message.client.pojo.SourceEvent;
import com.xie.message.client.support.MessagePublisher;
import com.xie.message.client.support.SendMsgCallback;
import com.xie.message.client.util.IdGeneratorUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jiaguofang
 * @version 1.0.0
 * @classname NewProduceService
 * @created 3/20/2019 21:47
 */
public class NewProduceService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessagePublisher messagePublisher;

    public void sendAsyncMsg(){
        Xobject obj = new Xobject();
        obj.setAge(20);
        obj.setName(IdGeneratorUtil.getRandomString(20));
        SourceEvent<Xobject> event = new SourceEvent<Xobject>(obj);
        event.setTopic("abc-2019-0320");
        messagePublisher.publishEvent(event, new SendMsgCallback() {
            @Override
            public void onSuccess(SendMsgResult sendResult) {
                logger.info(" 1111111111111111111 ");
            }

            @Override
            public void onException(Throwable e) {
                logger.error("22222222222222222", e);
            }
        });
    }
}
@Data
@Getter
@Setter
class Xobject {
    private String name;
    private int age;
}

