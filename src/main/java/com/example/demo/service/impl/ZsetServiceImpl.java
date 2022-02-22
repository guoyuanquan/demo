package com.example.demo.service.impl;

import com.example.demo.service.IZsetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;


@Slf4j
//@Service
public class ZsetServiceImpl implements IZsetService {

    private final String GEO_KEY = "ah-cities";

    /** redis 客户端 */
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public ZsetServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Long remove(){
        ZSetOperations<String,String> ops = redisTemplate.opsForZSet();
        return ops.removeRange(GEO_KEY,0,-1);
    }

}
