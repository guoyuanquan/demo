package com.example.demo.config;

import com.example.demo.util.RedisShipDataExpiredListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

//@Configuration
public class RedisConfig {
    @Autowired
    protected RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(this.redisConnectionFactory);
        return container;
    }

    @Bean
    public RedisShipDataExpiredListener redisShipDataExpiredListener() {
        return new RedisShipDataExpiredListener(this.redisMessageListenerContainer());
    }
}
