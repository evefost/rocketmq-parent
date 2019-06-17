package com.xie.test.a.controller;

import com.alibaba.fastjson.JSON;
import com.xie.test.a.beans.User;
import com.xie.test.a.buz.ServiceAServiceImpl;
import com.xie.test.a.producer.InterFaceA;
import com.xie.test.a.producer.InterfaceB;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/test")
public class TestTransactionController implements ApplicationContextAware {

    @Autowired
    private ServiceAServiceImpl transactionService;

    @Autowired(required = false)
    private InterFaceA interFaceA;

    @Autowired(required = false)
    private InterfaceB interfaceB;

    @RequestMapping(value = "/vjTestNoTag007", method = RequestMethod.GET)
    public String vjTestNoTag007 () {
        count++;
        transactionService.vjTestNoTag007(count);
        return "vjTestNoTag007-"+count;
    }

    @RequestMapping(value = "/vjTestNoTag", method = RequestMethod.GET)
    public String vjTest () {
        count++;
//        transactionService.publisherWithTransaction(count);
        transactionService.addUser(count);
        return "vjNoTag-"+count;
    }

    @RequestMapping(value = "/vjTestNoTrans", method = RequestMethod.GET)
    public String vjTestNoTrans () {
        count++;
//        transactionService.publisherWithTransaction(count);
        transactionService.addUserNoTrans(count);
        return "vjNoTag-"+count;
    }

    @RequestMapping(value = "/publishTopicCAddUser", method = RequestMethod.GET)
    public String publishTopicCAddUser() {
        count++;
        transactionService.publisherWithTransaction(count);
        return "noTag:"+count;
    }
    @RequestMapping(value = "/publishTopicC", method = RequestMethod.GET)
    public String publishTopicC() {
        count++;
        transactionService.publisherWithTransaction(count);
        return "noTag:"+count;
    }
    @RequestMapping(value = "/noTag", method = RequestMethod.GET)
    public String noTag() {
        count++;
       // transactionService.noTag(count);
        return "noTag:"+count;
    }

    @Transactional
    @RequestMapping(value = "/addUser3", method = RequestMethod.GET)
    public String addUser3() {
        count++;
        interFaceA.addUser3(new User());
        return "addUser3";
    }

    @RequestMapping(value = "/testA", method = RequestMethod.GET)
    public String testA() {
       interFaceA.aaa(new User());
        return "testA";
    }

    int count =0;
    @RequestMapping(value = "/addUser", method = RequestMethod.GET)
    public String addUser() {
        count++;
        transactionService.addUser(count);
        return "addUser:"+count;
    }
    @RequestMapping(value = "/addUser2", method = RequestMethod.GET)
    public String addUser2() {
        count++;
        transactionService.addUser2(count);
        return "addUser2:"+count;
    }


    @Autowired(required = false)
    TransactionMQProducer transactionMQProducer;

    @RequestMapping(value = "/transastion", method = RequestMethod.GET)
    public String transastion() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {

        User user =new User();
        user.setName("测试布式事件");
        Message message = new Message("dsfffffffffff","addUser",null,JSON.toJSONString(user).getBytes());

        message.setBody(JSON.toJSONString(user).getBytes());
       // transactionMQProducer.setTransactionListener(transactionListener);
        //TransactionSendResult result = transactionMQProducer.sendMessageInTransaction(message,null);
//        LocalTransactionState sendResult = result.getLocalTransactionState();
//        SendStatus sendStatus = result.getSendStatus();
//        System.out.println(Thread.currentThread().getId()+"发送结果："+sendStatus);
        //1.发送成功，处理业务,同步
        //2.发送失败，不处理业务
        //3.处理业务成功:commit 异步处理
        //4.处理业务失败:rollback 异步处理
        //5如果上述发送失败，只能等待服务端消息回查，但如果要使回查成功，
        // 3，4必须保存本地事务结果；否则最终的回查结果是丢弃事务消息，最终导致事务的不一致；

        transactionMQProducer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功。。。。");
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("发送失败。。。。");
            }
        });
        return "ccccddd:"+count;
    }

    ApplicationContext applicationContext;


    private TransactionListener transactionListener = new TransactionListener(){
        private AtomicInteger transactionIndex = new AtomicInteger(0);

        private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

        @Override
        public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            int value = transactionIndex.getAndIncrement();
            //模拟业务执行结果
            //1.发出pre事务消息,不等事务消息是否发送成功，处理务业代码并保存结果

            int status = value % 3;
            System.out.println(Thread.currentThread().getId()+"模拟业务:"+msg.getTransactionId()+":"+status);
            localTrans.put(msg.getTransactionId(), status);
            return LocalTransactionState.UNKNOW;
        }

        //本地事务回确认
        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {
            Integer status = localTrans.get(msg.getTransactionId());
            System.out.println("回查:"+msg.getTransactionId()+":"+status);
            if (null != status) {
                switch (status) {
                    case 0:
                        //产生者一值不确认
                        return LocalTransactionState.UNKNOW;
                    case 1:
                        return LocalTransactionState.COMMIT_MESSAGE;
                    case 2:
                        return LocalTransactionState.ROLLBACK_MESSAGE;
                }
            }
            return LocalTransactionState.COMMIT_MESSAGE;
        }
    };
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @RequestMapping(value = "/testB", method = RequestMethod.GET)
    public String testB() {
        count++;
        User user = new User();
        user.setName("testbbbbb");
        interfaceB.testB(user);
        return "addUser2:"+count;
    }

//    @GetMapping(value = "/getOrder")
//    public String getOrder(Integer integer) {
//        count++;
//        SiteFaultEventDTO user = new SiteFaultEventDTO();
//        List<Integer> faultIdList = new ArrayList<>();
//        faultIdList.add(integer);
//        user.setFaultIdList(faultIdList);
//        interFaceA.addUser3(user);
//        return "addUser2:"+count;
//    }
}