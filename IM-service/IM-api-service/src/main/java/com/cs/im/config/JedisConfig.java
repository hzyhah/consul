package com.cs.im.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Author : zfk
 * Data : 17:28
 */
@Configuration
public class JedisConfig {

    private Logger logger = LoggerFactory.getLogger(JedisConfig.class);

    public static JedisPool jedisPool = null;

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

    public JedisPool jedisPool() throws IOException {
        //读取配置
        Properties properties = new Properties();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream( "application.properties" );
        properties.load(inputStream);

        this.host = properties.getProperty( "spring.redis.host" );
        this.port = Integer.valueOf(properties.getProperty( "spring.redis.port"));
        this.password = properties.getProperty( "spring.redis.password" );
        this.timeout = Integer.valueOf(properties.getProperty( "spring.redis.timeout" ));
        this.maxIdle = Integer.valueOf(properties.getProperty( "spring.redis.jedis.pool.max-idle" ));
        this.maxActive = Integer.valueOf(properties.getProperty( "spring.redis.jedis.pool.max-active" ));
        this.minIdle = Integer.valueOf(properties.getProperty( "spring.redis.jedis.pool.min-idle" ));

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMinIdle(minIdle);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);

        logger.info("JedisPoll连接成功："+host+"\t"+port);
        return jedisPool;
    }

    @Bean
    public Jedis getJedis() throws IOException {
        if (jedisPool==null){
            JedisConfig.jedisPool = jedisPool();
            return jedisPool.getResource();
        }else{
            return jedisPool.getResource();
        }
    }

}