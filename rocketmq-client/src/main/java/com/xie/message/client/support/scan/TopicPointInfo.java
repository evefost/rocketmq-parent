package com.xie.message.client.support.scan;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xieyang on 18/7/15.
 */
public class TopicPointInfo {

    private boolean isProducer;

    private String envPrefix;

    private  Map<String/*evn+topic:tag*/,MethodInfo> topicMethodMappings = new HashMap<String, MethodInfo>();

    private  Map<Method,MethodInfo> methodInfoMappings = new HashMap<Method,MethodInfo>();

    private  Map<String/*topic*/,List<String>/*tag*/> topicTags = new HashMap<>();

    public Map<String, MethodInfo> getTopicMethodMappings() {
        return topicMethodMappings;
    }

    public Map<Method, MethodInfo> getMethodInfoMappings() {
        return methodInfoMappings;
    }

    public MethodInfo getMethodInfo(String key) {
        return topicMethodMappings.get(key);
    }

    public MethodInfo putMethodInfo(String key,MethodInfo methodInfo) {
        return topicMethodMappings.put(key,methodInfo);
    }

    public boolean isProducer() {
        return isProducer;
    }

    public String getEnvPrefix() {
        return envPrefix;
    }

    public void setEnvPrefix(String envPrefix) {
        this.envPrefix = envPrefix;
    }

    public void setProducer(boolean producer) {
        isProducer = producer;
    }

    public MethodInfo getMethodInfo(Method method) {
        return methodInfoMappings.get(method);
    }

    public MethodInfo putMethodInfo(Method method,MethodInfo methodInfo) {
        return methodInfoMappings.put(method,methodInfo);
    }
    public Map<String, List<String>> getTopicTags() {
        return topicTags;
    }
    public void setTopicTags(Map<String, List<String>> topicTags) {
        this.topicTags = topicTags;
    }

    public List<String> getTags(String topic) {
        return   this.topicTags.get(topic);
    }

    public void setTopicMethodMappings(
        Map<String, MethodInfo> topicMethodMappings) {
        this.topicMethodMappings = topicMethodMappings;
    }

    public void setMethodInfoMappings(
        Map<Method, MethodInfo> methodInfoMappings) {
        this.methodInfoMappings = methodInfoMappings;
    }
}
