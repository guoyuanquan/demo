package com.example.demo.util;


import com.example.demo.service.IGeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.nio.charset.StandardCharsets;

public class RedisShipDataExpiredListener extends KeyExpirationEventMessageListener {

    @Autowired
    protected StringRedisTemplate redisTemplate;
    @Autowired
    private IGeoService geoService;

    public RedisShipDataExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getBody(), StandardCharsets.UTF_8);
//        geoService.remove(key);
    }
}
