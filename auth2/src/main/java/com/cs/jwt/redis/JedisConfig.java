package com.cs.jwt.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
public class JedisConfig {

    private Logger logger = LoggerFactory.getLogger(JedisConfig.class);

    private static JedisPool jedisPool = null;

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    public JedisPool jedisPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);

        logger.info("JedisPoll连接成功："+host+"\t"+port);
        return jedisPool;
    }

    @Bean
    public Jedis getJedis(){
        if (jedisPool!=null){
            return jedisPool.getResource();
        }else{
            synchronized (this) {
                jedisPool = jedisPool();
            }
            return jedisPool.getResource();
        }
    }

}