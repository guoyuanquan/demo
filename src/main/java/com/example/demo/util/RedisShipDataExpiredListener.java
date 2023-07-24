package com.example.demo.util;


import com.example.demo.service.IGeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.PatternTopic;
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

    //监听指定的db
    @Override
    protected void doRegister(RedisMessageListenerContainer listenerContainer){
        int database=1;
        String topic ="__keyevent@"+database+"__:expired";
        PatternTopic patternTopic = new PatternTopic(topic);
        // 频道可以是多，多个传list
        listenerContainer.addMessageListener(this,patternTopic);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getBody(), StandardCharsets.UTF_8);
//        geoService.remove(key);
    }
}
