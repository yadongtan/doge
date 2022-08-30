package com.yadong.doge.registry.config;


import com.yadong.doge.config.ZookeeperProperties;
import com.yadong.doge.registry.client.RedisRegistryClient;
import com.yadong.doge.registry.client.ZookeeperRegistryClient;
import com.yadong.doge.registry.utils.RedisUtil;
import com.yadong.doge.registry.utils.ZkCuratorUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author YadongTan
 * @date 2022/8/30 10:54
 * @Description 配置连接注册中心的客户端信息
 */
@Configuration
public class DogeRegistryClientAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DogeRegistryClientAutoConfiguration.class);

    // 如果用户指定了zookeeper注册中心或者缺省, 启动zookeeper作为配置
    @ConditionalOnProperty(prefix = "doge", name = "registry"
            , havingValue = "zookeeper", matchIfMissing = true)
    @Configuration
    protected static class ZookeeperRegistryAutoConfiguration {
        // 如果用户指定了,那么这里不再定义
        @ConditionalOnMissingBean(CuratorFramework.class)
        @Bean
        public CuratorFramework curatorFramework(ZookeeperProperties zookeeperProperties) {
            //1、配置重试策略 5000：重试间隔 5：重试次数
            ExponentialBackoffRetry policy = new ExponentialBackoffRetry(
                    zookeeperProperties.getRetrySleepTime(),
                    zookeeperProperties.getMaxRetries());
            //2、构造Curator客户端
            CuratorFramework client = CuratorFrameworkFactory.builder()
                    .connectString(zookeeperProperties.getHost() + ":" + zookeeperProperties.getPort())
                    .connectionTimeoutMs(zookeeperProperties.getConnectionTimeout())
                    .sessionTimeoutMs(zookeeperProperties.getSessionTimeout())
                    .retryPolicy(policy).build();
            //3、启动客户端
            client.start();
            logger.info("zookeeper启动成功，获取到客户端链接");
            return client;
        }

        @ConditionalOnBean({CuratorFramework.class})
        @Bean
        public ZkCuratorUtil zkCuratorUtil(CuratorFramework curatorFramework){
            return new ZkCuratorUtil(curatorFramework);
        }

        @ConditionalOnBean({CuratorFramework.class})
        @Bean
        public ZookeeperRegistryClient zookeeperRegistryClient(ZkCuratorUtil zkCuratorUtil){
            return new ZookeeperRegistryClient(zkCuratorUtil);
        }
    }

    // 用户必须指定doge.registry = redis, 才会采用redis作为配置中心并配置redis客户端
    @ConditionalOnProperty(prefix = "doge", name="registry", havingValue = "redis")
    @Configuration
    protected static class RedisRegistryAutoConfiguration{

        @ConditionalOnMissingBean({RedisTemplate.class})
        @Bean
        public RedisTemplate<String, Object>
        redisTemplate(RedisConnectionFactory connectionFactory) {
            RedisTemplate<String, Object> redisTemplate = new
                    RedisTemplate<>();
            //序列化器
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer
                    (new JdkSerializationRedisSerializer());
            redisTemplate.setConnectionFactory(connectionFactory);
            logger.info("redis启动成功，获取到客户端链接");
            return redisTemplate;
        }



        @ConditionalOnBean({RedisTemplate.class})
        @Bean
        public RedisUtil redisUtil(RedisTemplate<String, Object> redisTemplate){
            return new RedisUtil(redisTemplate);
        }

        @ConditionalOnBean({RedisTemplate.class})
        @Bean
        public RedisRegistryClient redisRegistryClient(RedisUtil redisUtil){
            return new RedisRegistryClient(redisUtil);
        }
    }


}
