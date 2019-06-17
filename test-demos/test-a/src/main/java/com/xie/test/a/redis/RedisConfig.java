package com.xie.test.a.redis;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Cluster;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * @ClassName RedisConfig
 * @Description TODO
 * @Author jiaguofang
 * @CreateAt 3/6/2019 14:14
 * @Version 1.0.0
 */
//@Configuration
//@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    private Logger logger = LoggerFactory.getLogger(RedisConfig.class);
    @Autowired
    private RedisProperties redisProperties;

    // vjia
//    @Bean
    public JedisCluster getJedisCluster() {
        JedisCluster jedisCluster;
        Cluster cluster = redisProperties.getCluster();
        List<String> hostPorts = cluster.getNodes();
        Set<HostAndPort> nodes = Sets.newHashSet();
        for (String hostPort : hostPorts) {
            String[] hostPortPair = StringUtils.split(hostPort, ":");
            nodes.add(new HostAndPort(hostPortPair[0], Integer.valueOf(hostPortPair[1])));
        }
        Pool pool = redisProperties.getPool();
        if (null == pool) {
            pool = new Pool();
            logger.warn("Redis没有配置线程池信息，系统将采用默认值");
        }
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(pool.getMaxIdle());
        poolConfig.setMinIdle(pool.getMinIdle());
        poolConfig.setMaxTotal(pool.getMaxActive());
        poolConfig.setMaxWaitMillis(pool.getMaxWait());

        if (StringUtils.isBlank(redisProperties.getPassword())) {
            jedisCluster = new JedisCluster(nodes, redisProperties.getTimeout(), poolConfig);
        } else {
            // TODO vj SoTimeout, MaxAttempts
            jedisCluster = new JedisCluster(nodes, redisProperties.getTimeout(), 0, 0, redisProperties.getPassword(), poolConfig);
        }

        return jedisCluster;
    }


}
