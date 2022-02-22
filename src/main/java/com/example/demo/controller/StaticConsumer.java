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

    @PostConstruct
    public void receiveDynamic() {
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty(PropertyKeyConst.GROUP_ID, "GID_sg_mq_taiji_stg_universe_ais_gj_jt");
        consumerProperties.setProperty(PropertyKeyConst.AccessKey, "6f181321efd9449ba45d2c69796b17f5");
        consumerProperties.setProperty(PropertyKeyConst.SecretKey, "OopH5XhRhlZfCg/O7iFaWotHkLQ=");
        consumerProperties.setProperty(PropertyKeyConst.NAMESRV_ADDR, "http://mq.namesrv.paas.sgpt.gov:9876");
        consumerProperties.setProperty(PropertyKeyConst.InstanceName, "sgpt_sgzyc");
        //集群方式订阅
//        consumerProperties.put(PropertyKeyConst.MessageModel,PropertyValueConst.CLUSTERING);
        //广播方式订阅
//        consumerProperties.put(PropertyKeyConst.MessageModel, PropertyValueConst.BROADCASTING);
        Consumer consumer = ONSFactory.createConsumer(consumerProperties);
        consumer.subscribe("zdk2", "stg_universe_ais_gj_jt", new MessageListener(){

            @Override
            public Action consume(Message message, ConsumeContext consumeContext) {
                String text = new String(message.getBody());
                Map<String,String> map = JSONObject.parseObject(text,Map.class);
                String userId = map.get("userid");
                String result = redisTemplate.opsForValue().get(userId);
                if (StringUtils.hasLength(result)){
                    sameNum++;
                }
                count++;
                log.info("总数："+count);
                log.info("匹配数:"+sameNum);
                return Action.CommitMessage;
            }
        });
        consumer.start();
        System.out.println("Static Consumer start success.");
    }

    public void addInfo(String text) {
        try {
            final String now = DateUtil.format(new Date(), "yyyy-MM-ddHH:mm:sss");
            DynamicReceive dynamicReceive =new DynamicReceive();
            dynamicReceive= JSON.parseObject(text,DynamicReceive.class);
            Set<CityInfo> cityInfos = new HashSet<CityInfo>();
            CityInfo cityInfo1 =new CityInfo();
            cityInfo1.setCityName(text);
            cityInfo1.setLatitude(dynamicReceive.getLatitude());
            cityInfo1.setLongitude(dynamicReceive.getLongitude());
            cityInfos.add(cityInfo1);
//            geoService.saveCityInfoToRedis(cityInfos);
            redisService.setRedis(text,now);
        }catch (Exception e){
            e.printStackTrace();
        }

    }



}
