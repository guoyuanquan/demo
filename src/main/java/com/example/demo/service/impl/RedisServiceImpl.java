package com.example.demo.service.impl;

import com.example.demo.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

//@Service
public class RedisServiceImpl implements IRedisService {

    @Autowired
    protected StringRedisTemplate redisTemplate;

    @Override
    public void setRedis(String key,String value){
        this.redisTemplate.opsForValue().set(key,value,12, TimeUnit.HOURS);
    }
}
