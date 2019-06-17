package com.xie.message.client.support.scan;

import com.alibaba.fastjson.JSON;
import com.xie.message.client.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static com.google.common.collect.Sets.newHashSet;


public class TopicDeclareBeanRegistrar implements ImportBeanDefinitionRegistrar,
        ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware{

    protected final Logger logger = LoggerFactory.getLogger(TopicDeclareBeanRegistrar.class);

    static final String ENV_PREFIX ="${app.env.prefix:}";

    public static final String CONSUMER_INFO = "consumerInfo";

    public static final String PRODUCER_INFO = "producerInfo";


    protected ResourceLoader resourceLoader;

    protected ClassLoader classLoader;

    protected Environment environment;

    private  String currentEnv;

    private static final Set<String> baseTypes = newHashSet(
            "int",
            "date",
            "string",
            "double",
            "float",
            "boolean",
            "byte",
            "object",
            "long",
            "date-time",
            "file",
            "biginteger",
            "bigdecimal");

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        logger.info("自动扫描主题信息...");
        Set<String> basePackages = getBasePackages(metadata);
        String nameSrvAddr = environment.getProperty("spring.extend.mq.serverAddr");
        if(StringUtils.isEmpty(nameSrvAddr)){
            logger.warn("启用topic扫描，但没配置服务地址");
            return;
        }
        currentEnv = resolve(ENV_PREFIX);
        if(logger.isDebugEnabled()){
            logger.debug("rocket mq 当前环境前辍:{}",currentEnv);
        }
        String producerEnable = environment.getProperty("spring.extend.mq.producer.enable","false");
        String consumerEnable = environment.getProperty("spring.extend.mq.consumer.enable","false");
        boolean pdenable = Boolean.parseBoolean(producerEnable);
        boolean csenable = Boolean.parseBoolean(consumerEnable);
        if(pdenable == false && csenable ==false){
            logger.warn("生产端消费端都没有启用,生产端虚拟接口将无法注入,可能导致项目启动失败");
        }
        TopicPointInfo producerInfo = new TopicPointInfo();
        producerInfo.setProducer(true);
        producerInfo.setEnvPrefix(currentEnv);
        if(pdenable) {
            try {
                scanProducers(basePackages,producerInfo, registry);
            } catch (ClassNotFoundException e) {
                logger.error("扫描mq producer 接口失败",e);
            }
        }
        TopicPointInfo consumerInfo = new TopicPointInfo();
        consumerInfo.setProducer(false);
        consumerInfo.setEnvPrefix(currentEnv);
        if(csenable){
            try {
                scanConsumers(basePackages,consumerInfo);
                GenericBeanDefinition invokerBd = new GenericBeanDefinition();
                invokerBd.setBeanClass(ConsumerInvoker.class);
                MutablePropertyValues values = new MutablePropertyValues();
                invokerBd.setPropertyValues(values);
                registry.registerBeanDefinition("autoInvoker", invokerBd);
            } catch (ClassNotFoundException e) {
                logger.error("扫描mq consumer 失败",e);
            }
        }
        //注入生者信息
        injectMethodInfo(PRODUCER_INFO,producerInfo,registry);
        //注入consumer信息
        injectMethodInfo(CONSUMER_INFO,consumerInfo,registry);
    }

    private void injectMethodInfo(String beanName, TopicPointInfo virtualPointInfo, BeanDefinitionRegistry registry ){

        //按环境处理主题
        Map<Method, MethodInfo> methodInfoMappings = virtualPointInfo.getMethodInfoMappings();
        Map<String, MethodInfo> topicMethodMappings = new HashMap<>(methodInfoMappings.size());
        methodInfoMappings.forEach((method,methodInfo)->{
            String newTopic = currentEnv+methodInfo.getTopic();
            String key = newTopic;
            if(methodInfo.getTag() != null ) {
                key = key + ":"+methodInfo.getTag();
            }
            methodInfo.setTopic(newTopic);
            topicMethodMappings.put(key,methodInfo);
        });
        virtualPointInfo.setTopicMethodMappings(topicMethodMappings);

        Map<String, List<String>> srcTopicTags = virtualPointInfo.getTopicTags();
        Map<String, List<String>> topicTags = new HashMap<>(srcTopicTags.size());
        srcTopicTags.forEach((srcTopic,tags)->{
            String newTopic = currentEnv+srcTopic;
            topicTags.put(newTopic,tags);
        });
        virtualPointInfo.setTopicTags(topicTags);

        GenericBeanDefinition consumerBd = new GenericBeanDefinition();
        consumerBd.setBeanClass(TopicPointInfo.class);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        consumerBd.setPropertyValues(propertyValues);
        propertyValues.add("topicMethodMappings", topicMethodMappings);
        propertyValues.add("methodInfoMappings", methodInfoMappings);
        propertyValues.add("topicTags", topicTags);
        propertyValues.add("producer", virtualPointInfo.isProducer());
        propertyValues.add("envPrefix", virtualPointInfo.getEnvPrefix());
        registry.registerBeanDefinition(beanName, consumerBd);
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableScanTopic.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("producerPackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        for (String pkg : (String[]) attributes.get("consumerPackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        if (basePackages.isEmpty()) {
            basePackages.add(
                    ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }

    private void scanProducers(Set<String> basePackages,TopicPointInfo producerInfo,
                                     BeanDefinitionRegistry registry) throws ClassNotFoundException {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(
                Producer.class);
        scanner.addIncludeFilter(annotationTypeFilter);

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner
                    .findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    // verify annotated class is an interface
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                    String name = "producerApi$" + candidateComponent.getBeanClassName();
                    registerProducerApi(registry, name, annotationMetadata,producerInfo);
                }
            }
        }
        logger.info("当前应用扫描到生产topics " + JSON.toJSONString(producerInfo.getTopicTags()));
    }


    protected void registerProducerApi(BeanDefinitionRegistry registry, String name,
                                      AnnotationMetadata annotationMetadata,TopicPointInfo producerInfo) throws ClassNotFoundException {
        String beanName = name;
        logger.debug("即将创建的实例名:" + beanName);
        String beanClassName = annotationMetadata.getClassName();
        parseVirtualInfo(beanClassName, producerInfo,true);
        Map<String, Object> attritutes = annotationMetadata.getAnnotationAttributes(Topic.class.getCanonicalName());
        BeanDefinitionBuilder definition = BeanDefinitionBuilder
                .genericBeanDefinition(ProducerFactoryBean.class);
        definition.addPropertyValue("name", name);
        definition.addPropertyValue("type", beanClassName);
        if(attritutes != null){
            definition.addPropertyValue("topic", attritutes.get("value"));
        }
        definition.addPropertyValue("producerInfo", producerInfo);
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        beanDefinition.setPrimary(false);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, beanName,
                new String[]{});
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    public void scanConsumers(Set<String> basePackages, TopicPointInfo consumerInfo) throws ClassNotFoundException {
        //扫描指定的包，过滤出只打topic 及 tag标签的接口或类
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        AnnotationTypeFilter inFilter = new AnnotationTypeFilter(
                Consumer.class);
        scanner.addIncludeFilter(inFilter);
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner
                    .findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    String beanClassName = candidateComponent.getBeanClassName();
                    parseVirtualInfo(beanClassName, consumerInfo,false);
                }
            }
        }
        logger.info("当前应用扫描到消费者topics " + JSON.toJSONString(consumerInfo.getTopicTags()));
    }

    private void parseVirtualInfo(String beanClassName, TopicPointInfo pointInfo, boolean isProducer) throws ClassNotFoundException {

        Class<?> targetClass = classLoader.loadClass(beanClassName);
        Topic classTopicAnno = targetClass.getAnnotation(Topic.class);
        String classTopic = null;
        if(classTopicAnno != null){
            classTopic = classTopicAnno.value();
        }
        Method[] methods;
        if (targetClass.isInterface()) {
            methods = targetClass.getMethods();
        } else {
            methods = targetClass.getDeclaredMethods();
        }
        for (Method method : methods) {
            method.setAccessible(true);
            Topic methodTopicAnno = method.getAnnotation(Topic.class);
            Tag tagAnno = method.getAnnotation(Tag.class);
            if(methodTopicAnno == null && tagAnno== null){
                continue;
            }
            String methodTopic = null;
            if(methodTopicAnno !=null){
                methodTopic = methodTopicAnno.value();
            }
            String targetTopic = methodTopic==null?classTopic:methodTopic;
            if(StringUtils.isEmpty(targetTopic)){
                throw new RuntimeException("主题不能为空"+targetClass.getName()+"."+method.getName());
            }
            targetTopic = resolve(targetTopic);
            MethodInfo methodInfo = new MethodInfo();
            pointInfo.putMethodInfo(method,methodInfo);

            methodInfo.setTargetClass(targetClass);
            methodInfo.setMethod(method);
            methodInfo.setTrans(method.isAnnotationPresent(TransMsg.class));
            methodInfo.setTopic(targetTopic);
            String targetTag = null;
            String key;
            if (tagAnno == null) {
                key = targetTopic;
            }else {
                targetTag = resolve(tagAnno.value());
                key = targetTopic + ":" + targetTag;
            }
            methodInfo.setTag(targetTag);
            List<String> tags = pointInfo.getTags(targetTopic);
            if (tags == null) {
                tags = new ArrayList<>();
            }
            //检测是否有重复的设置
            checkScanInfo(key,pointInfo,methodInfo);
            pointInfo.putMethodInfo(key, methodInfo);
            if(targetTag != null) {
                tags.add(targetTag);
            } else if( !isProducer ) {
                tags.add("*");
            }
            pointInfo.getTopicTags().put(targetTopic, tags);
        }

    }

    private void checkScanInfo(String key, TopicPointInfo pointInfo, MethodInfo methodInfo) {
        MethodInfo rs = pointInfo.getMethodInfo(key);
        Method method = methodInfo.getMethod();
        String targetClassName = methodInfo.getTargetClass().getName();
        if (rs != null) {
            String erromsg = rs.getTargetClass().getName() + " & " + targetClassName;
            throw new RuntimeException("  topic&&tag: " + key + " already exist in " + erromsg);
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes != null && parameterTypes.length > 1) {
            throw new RuntimeException("消息参数仅支持单个且为非基本类型" + targetClassName + "." + method.getName());
        } else if (parameterTypes != null && parameterTypes.length == 1 && isBaseType(parameterTypes[0].getSimpleName())) {
            throw new RuntimeException("消息参数仅支持单个且为非基本类型" + targetClassName + "." + method.getName());
        }
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {

            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                if (beanDefinition.getMetadata().isIndependent()) {
                    // TODO until SPR-11711 will be resolved
                    if (beanDefinition.getMetadata().isInterface()
                            && beanDefinition.getMetadata()
                            .getInterfaceNames().length == 1
                            && Annotation.class.getName().equals(beanDefinition
                            .getMetadata().getInterfaceNames()[0])) {
                        try {
                            Class<?> target = ClassUtils.forName(
                                    beanDefinition.getMetadata().getClassName(),
                                    TopicDeclareBeanRegistrar.this.classLoader);
                            return !target.isAnnotation();
                        } catch (Exception ex) {
                            this.logger.error(
                                    "Could not load target class: "
                                            + beanDefinition.getMetadata().getClassName(),
                                    ex);

                        }
                    }
                    return true;
                }
                return false;

            }
        };
    }
    public static boolean isBaseType(String typeName) {
        return baseTypes.contains(typeName.toLowerCase());
    }

    private String resolve(String value) {
        if (StringUtils.hasText(value)) {
            return this.environment.resolvePlaceholders(value);
        }
        return value;
    }

}
