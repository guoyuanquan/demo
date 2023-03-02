/**
 * Copyright (C) 2010-2016 Alibaba Group Holding Limited
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo.controller;


import cn.hutool.core.date.DateUtil;
import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSONObject;
import com.example.demo.model.CityInfo;
import com.example.demo.model.DynamicReceive;
import com.example.demo.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * MQ 接收消息示例 Demo
 */
@Slf4j
//@Component
public class StaticConsumer {

//    @Autowired
//    private IGeoService geoService;
    @Autowired
    private IRedisService redisService;
    @Autowired
    protected StringRedisTemplate redisTemplate;

    protected int count=0;
    protected int sameNum=0;

//    @PostConstruct
    public static void main(String args[]) {
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty(PropertyKeyConst.GROUP_ID, "GID_taiji_fukan");
        consumerProperties.setProperty(PropertyKeyConst.AccessKey, "6f181321efd9449ba45d2c69796b17f5");
        consumerProperties.setProperty(PropertyKeyConst.SecretKey, "OopH5XhRhlZfCg/O7iFaWotHkLQ=");
        consumerProperties.setProperty(PropertyKeyConst.NAMESRV_ADDR, "http://mq.namesrv.paas.sgpt.gov:9876");
        consumerProperties.setProperty(PropertyKeyConst.InstanceName, "sgpt_sgzyc");
        //集群方式订阅
//        consumerProperties.put(PropertyKeyConst.MessageModel,PropertyValueConst.CLUSTERING);
        //广播方式订阅
//        consumerProperties.put(PropertyKeyConst.MessageModel, PropertyValueConst.BROADCASTING);
        Consumer consumer = ONSFactory.createConsumer(consumerProperties);
        consumer.subscribe("zdk2", "stg_universe_ais_gj_dt", new MessageListener(){

            @Override
            public Action consume(Message message, ConsumeContext consumeContext) {
                String text = new String(message.getBody());
                log.info("接收数据："+text);
//                Map<String,String> map = JSONObject.parseObject(text,Map.class);
//                String receiveTime = map.get("receive_time");
//                addInfo(receiveTime);
                return Action.CommitMessage;
            }
        });
        consumer.start();
        System.out.println("Static Consumer start success.");
    }


//    public void addInfo(String receiveTime) {
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date=sdf.parse(receiveTime);
//            Date currentTime = new Date();
//            System.out.println(receiveTime);
//            System.out.println(sdf.format(currentTime));
//            System.out.println((currentTime.getTime()-date.getTime()));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }



}
