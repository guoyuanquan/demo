package com.example.demo.controller.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
//@Component
public class KafkaCountOut {
    @Autowired
    protected StringRedisTemplate redisTemplate;

    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void printCount(){
        Set set = redisTemplate.keys("*");
        log.info("redis数量"+set.size());
    }
}
