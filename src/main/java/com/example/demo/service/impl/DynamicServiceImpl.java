package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.mapper.DynamicMapper;
import com.example.demo.model.DynamicReceive;
import com.example.demo.service.IDynamicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Slf4j
//@Service
public class DynamicServiceImpl extends ServiceImpl<DynamicMapper, DynamicReceive> implements IDynamicService  {

    @Resource
    private DynamicMapper dynamicMapper;


    @Override
    public void save(String msg){
        DynamicReceive dynamicReceive =new DynamicReceive();
        dynamicReceive = JSON.parseObject(msg,DynamicReceive.class);
        dynamicReceive.setUuid(UUID.randomUUID().toString());
        dynamicMapper.insert(dynamicReceive);

    }

}
