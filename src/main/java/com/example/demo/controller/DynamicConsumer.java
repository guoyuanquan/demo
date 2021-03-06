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


import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSONObject;
import com.example.demo.model.CityInfo;
import com.example.demo.model.DynamicReceive;
import com.example.demo.service.IGeoService;
import com.example.demo.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

import cn.hutool.core.date.DateUtil;

/**
 * MQ 接收消息示例 Demo
 */
@Slf4j
//@Component
public class DynamicConsumer {

//    @Autowired
//    private IGeoService geoService;
    @Autowired
    private IRedisService redisService;


    @PostConstruct
    public void receiveDynamic() {
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty(PropertyKeyConst.GROUP_ID, "GID_sg_mq_taiji_stg_universe_ais_gj_dt");
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
                log.info("text:"+text);
//                Map<String,String> map = JSONObject.parseObject(text,Map.class);
//                String userId = map.get("userid");
//                if (userId.equals("413376420")){
//                    log.info("当前时间："+DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//                    log.info(text);
////                    final String now = DateUtil.format(new Date(), "yyyy-MM-ddHH:mm:sss");
//                    redisService.setRedis(userId,userId);
//                }
//                addInfo(text);

                return Action.CommitMessage;
            }
        });
        consumer.start();
        System.out.println("Dynamic Consumer start success.");
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
