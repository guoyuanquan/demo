package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
public class RedisTest {

    @Autowired
    protected StringRedisTemplate redisTemplate;


    @PostConstruct
    void redisTest(){
        String m="1";
        String values=redisTemplate.opsForValue().get(m);
        System.out.println(values);
    }
}
