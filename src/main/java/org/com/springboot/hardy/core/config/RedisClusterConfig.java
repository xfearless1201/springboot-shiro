package org.com.springboot.hardy.core.config;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * 
 * @ClassName RedisClusterConfig
 * @Description 缓存集群配置类
 * @author Hardy
 * @Date 2018年12月21日 下午4:28:38
 * @version 1.0.0
 */
@Configuration
@ConditionalOnClass({ JedisCluster.class })
public class RedisClusterConfig {

    @Value("${spring.redis.cache.clusterNodes}")
    private String clusterNodes;
    @Value("${spring.redis.cache.password}")
    private String password;
    @Value("${spring.redis.cache.commandTimeout}")
    private Integer commandTimeout;

    @Bean
    public JedisCluster getJedisCluster() {
        String[] serverArray = clusterNodes.split(",");
        Set<HostAndPort> nodes = new HashSet<>();
        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        return new JedisCluster(nodes, commandTimeout, commandTimeout, 2, password, new GenericObjectPoolConfig());
    }
}
