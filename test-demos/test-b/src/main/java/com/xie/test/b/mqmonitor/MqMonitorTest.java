package com.xie.test.b.mqmonitor;

import com.xie.message.client.config.RocketmqProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.common.admin.ConsumeStats;
import org.apache.rocketmq.common.admin.OffsetWrapper;
import org.apache.rocketmq.common.admin.TopicOffset;
import org.apache.rocketmq.common.admin.TopicStatsTable;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.body.Connection;
import org.apache.rocketmq.common.protocol.body.ConsumerConnection;
import org.apache.rocketmq.common.protocol.body.GroupList;
import org.apache.rocketmq.common.protocol.body.TopicList;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.common.protocol.heartbeat.SubscriptionData;
import org.apache.rocketmq.common.protocol.route.TopicRouteData;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping(value = "mq")
public class MqMonitorTest {
    private static final Logger logger = LoggerFactory.getLogger(MqMonitorTest.class);
    @Autowired
    private RocketmqProperties rocketmqProperties;
    private DefaultMQAdminExt defaultMQAdminExt;

    private StringBuffer totalResultSB;

    @RequestMapping(value = "/m1")
    @ResponseBody
    public Map<String, Object> m1() throws MQClientException {
        logger.info(" goooooooooooooooooooooooooooooo  ");
        totalResultSB = new StringBuffer();
        Map<String, Object> map = new HashMap<>();
        String namesrvAddr = rocketmqProperties.getServerAddr();
        defaultMQAdminExt = new DefaultMQAdminExt();
        defaultMQAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
        defaultMQAdminExt.setNamesrvAddr(namesrvAddr);
        defaultMQAdminExt.start();
        getDiffNum();

        DefaultMQProducer defaultMQProducer=new DefaultMQProducer();

        return map;
    }

    private long getDiffNum() {
        long diffTotal = 0;
        try {
            // group name : 41-xhg-device-service
//        ConsumeStats consumeStats=defaultMQAdminExt.examineConsumeStats(this.groupName);
            // a: xhg-mq-client001, 41-xhg-device-service
            GroupList a = defaultMQAdminExt.queryTopicConsumeByWho("xhg41-order-save");
            // b: 10.10.10.218:9876, 10.10.10.219:9876
            List<String> b=defaultMQAdminExt.getNameServerAddressList();
            // c: orderTopicConf=null
            // QueueData [brokerName=broker-a1, readQueueNums=4, writeQueueNums=4, perm=6, topicSynFlag=0]
            // BrokerData [brokerName=broker-a1, brokerAddrs={0=10.10.10.218:10911, 1=10.10.10.220:10911}]
            TopicRouteData c=defaultMQAdminExt.examineTopicRouteInfo("xhg41-order-save");
            // d:  size = 59
            TopicList d=defaultMQAdminExt.fetchTopicsByCLuster("DefaultClusterAug");
            // e:  size = 59
            TopicList e=defaultMQAdminExt.fetchAllTopicList();
            {
                logger.info(" --------------------------------------------- ");
                Set<String> list=e.getTopicList();
                logger.info("now is having {} topic in total", list.size());
                for(String topic:list){
                    // 获取所有topic各自的 consumerGroup
                    this.processTopicGroupList(topic);
                    // 不考虑consumerGroup的情况下，获取所有的 maxOffset、minOffset
                    this.processTopicStatsTable(topic);
                }
                logger.info(" --------------------------------------------- ");
                for(String topic:list){
                    // 获取所有topic各自的 consumerGroup
                    this.processTopicGroupList(topic);
                }
            }
            // f:   size = 4
            // "MessageQueue [topic=xhg41-order-save, brokerName=broker-a1, queueId=3]" ->
            // "MessageQueue [topic=xhg41-order-save, brokerName=broker-a1, queueId=1]" ->
            // "MessageQueue [topic=xhg41-order-save, brokerName=broker-a1, queueId=2]" ->
            // "MessageQueue [topic=xhg41-order-save, brokerName=broker-a1, queueId=0]" ->
            TopicStatsTable f=defaultMQAdminExt.examineTopicStats("xhg41-order-save");
            processTopicStatsTable("xhg41-order-save");
            {
            }
            int x=1;

            String consumerGroup = "41-xhg-device-service";
            ConsumerConnection cc = defaultMQAdminExt.examineConsumerConnectionInfo(consumerGroup);
            {
                int aa=cc.computeMinVersion();
                logger.info("aa={}, cc={}",aa, ToStringBuilder.reflectionToString(cc));
                HashSet<Connection> bb=cc.getConnectionSet();
                Connection gg=bb.iterator().next();
                String ga=gg.getClientAddr();
                logger.info("ga={},bb={}", ga, ToStringBuilder.reflectionToString(bb));
                ConsumeFromWhere dd=cc.getConsumeFromWhere();
                String ddname=dd.name();
                int ddordinal=dd.ordinal();
                logger.info("ddname:{}, ddordinal:{},dd:{}",ddname,ddordinal,dd);
                MessageModel ee=cc.getMessageModel();
                ConcurrentMap<String, SubscriptionData> ff=cc.getSubscriptionTable();

            }

            doCalculateMessageLasting();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return diffTotal;
    }

    private void processTopicGroupList(String topic_) {
        try{
            GroupList a = defaultMQAdminExt.queryTopicConsumeByWho(topic_);
            GroupList groupList=a;
            HashSet<String> set=groupList.getGroupList();
            if(set==null || set.size()==0){
                logger.info("topic [ {} ] has no consumeGroup.",topic_);
            } else {
                StringBuffer sb=new StringBuffer();
                for(String group:set){
                    sb.append(group).append(" , ");
                }
                logger.info("topic [ {} ] has consumeGroup: {}.",topic_, sb.substring(0, sb.length()-2));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void processTopicStatsTable(String topic_) {
        try {
            TopicStatsTable f = defaultMQAdminExt.examineTopicStats(topic_);
            TopicStatsTable topicStatsTable = f;
            HashMap<MessageQueue, TopicOffset> x = topicStatsTable.getOffsetTable();
            for (MessageQueue messageQueue : x.keySet()) {
                String brokerName = messageQueue.getBrokerName();
                String topic = messageQueue.getTopic();
                TopicOffset topicOffset=x.get(messageQueue);
                long lastUpdateTimestamp=topicOffset.getLastUpdateTimestamp();
                long maxOffset=topicOffset.getMaxOffset();
                long minOffset=topicOffset.getMinOffset();
                logger.info("topic:{},brokerName:{},lastUpdateTimestamp:{},maxOffset:{},minOffset:{} ",
                        topic,brokerName,lastUpdateTimestamp,maxOffset,minOffset);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void doCalculateMessageLasting() {
        try {
            TopicList e = defaultMQAdminExt.fetchAllTopicList();
            Set<String> list=e.getTopicList();
            logger.info("now is having {} topic in total", list.size());
            for(String topic:list){
                if(topic.startsWith(MixAll.RETRY_GROUP_TOPIC_PREFIX)){
                    String consumerGroup = topic.substring(MixAll.RETRY_GROUP_TOPIC_PREFIX.length());
                    // 先 skip掉 retry_group
                    logger.warn("meet RETRY_GROUP, continue. topic:[{}], consumerGroup:[{}]", topic, consumerGroup);
                    continue;
                }
                // 如果含有consumerGroup，则直接计算其msg剩余量
                boolean hasGroup = judgeHasAnyGroupListForTopic(topic);
                // 否则，没有consumerGroup的情况下，直接计算 maxOffset、minOffset
                if( !hasGroup) {
                    calculateTopicMessageLastingWithoutGroup(topic);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(this.totalResultSB);
    }

    private boolean judgeHasAnyGroupListForTopic(String topic_) {
        boolean hasGroup=false;
        try{
            GroupList a = defaultMQAdminExt.queryTopicConsumeByWho(topic_);
            GroupList groupList=a;
            HashSet<String> set=groupList.getGroupList();
            if(set==null || set.size()==0){
                hasGroup=false;
                logger.info("topic [ {} ] has no consumeGroup.",topic_);
            } else {
                hasGroup=true;
                StringBuffer sb=new StringBuffer();
                for(String group:set){
                    sb.append(group).append(" , ");
                }
                logger.info("topic [ {} ] has consumeGroup: {}.",topic_, sb.substring(0, sb.length()-2));
                // 如果含有consumerGroup，则直接计算其msg剩余量
                calculateTopicMessageLasting(topic_, set);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return hasGroup;
    }

    private void calculateTopicMessageLasting(String topic, HashSet<String> groupNameSet) {
        long diffTotal=0;
        try{
            for(String group:groupNameSet){
                try {
                    long groupDiffTotal = 0;
                    // //当消费端未消费时，此方法会报错 ??
                    ConsumeStats consumeStats = defaultMQAdminExt.examineConsumeStats(group, topic);
                    HashMap<MessageQueue, OffsetWrapper> mqMap = consumeStats.getOffsetTable();
                    //遍历所有的队列，计算堆积量
                    for (MessageQueue mq : mqMap.keySet()) {
                        OffsetWrapper offsetWrapper = mqMap.get(mq);
                        long diff = offsetWrapper.getBrokerOffset() - offsetWrapper.getConsumerOffset();
                        groupDiffTotal += diff;
                        diffTotal += diff;
                    }
                    logger.info("===============> topic [ {} ], group [ {} ], groupTotal left: [ {} ]", topic, group, groupDiffTotal);
                    totalResultSB.append(String.format("===============> topic [ %s ], group [ %s ], groupTotal left: [ %s ]", topic, group, groupDiffTotal)).append("\r\n");
                } catch (Exception e){
                    // sample error:
                    // { org.apache.rocketmq.client.exception.MQClientException: CODE: 206  DESC: Not found the consumer group consume stats,
                    //      because return offset table is empty, maybe the consumer not consume any message }
                    logger.error("211.error occur with topic [{}], group [{}], try another way",topic, group, e);
                    calculateTopicMessageLastingWithoutGroup(topic);
                }
            }
            logger.info("===============>[withGroup] topic [ {} ],  Total left: [ {} ]",topic,diffTotal);
            totalResultSB.append(String.format("===============>[withGroup] topic [ %s ],  Total left: [ %s ]",topic,diffTotal)).append("\r\n");
        } catch (Exception e){
            logger.error("209.error occur", e);
        }
    }

    private void calculateTopicMessageLastingWithoutGroup(String topic_) {
        long diffTotal=0;
        try {
            TopicStatsTable f = defaultMQAdminExt.examineTopicStats(topic_);
            TopicStatsTable topicStatsTable = f;
            HashMap<MessageQueue, TopicOffset> x = topicStatsTable.getOffsetTable();
            for (MessageQueue messageQueue : x.keySet()) {
                String brokerName = messageQueue.getBrokerName();
                String topic = messageQueue.getTopic();
                TopicOffset topicOffset=x.get(messageQueue);
                long lastUpdateTimestamp=topicOffset.getLastUpdateTimestamp();
                long maxOffset=topicOffset.getMaxOffset();
                long minOffset=topicOffset.getMinOffset();
                // BenchmarkTest 有1024个 读写queue ！！
                if( !"BenchmarkTest".equalsIgnoreCase(topic)) {
                    logger.info("topic:{},brokerName:{},lastUpdateTimestamp:{},maxOffset:{},minOffset:{} ",
                            topic,brokerName,lastUpdateTimestamp,maxOffset,minOffset);
                }
                long diff = maxOffset-minOffset;
                diffTotal += diff;
            }
            logger.info("===============>[withoutGroup] topic [ {} ],  Total left: [ {} ]",topic_,diffTotal);
            totalResultSB.append(String.format("===============>[withoutGroup] topic [ %s ],  Total left: [ %s ]",topic_,diffTotal)).append("\r\n");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
