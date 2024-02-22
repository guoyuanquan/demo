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
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSONObject;

import com.example.demo.model.FusionRadar;
import com.example.demo.service.IDynamicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
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
//    @Autowired
//    private IRedisService redisService;
    @Resource
    private IDynamicService dynamicService;

    private SimpleDateFormat pdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//    @PostConstruct
    public void receiveDynamic() {
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty(PropertyKeyConst.GROUP_ID, "GID_taiji_ax_stg_jmrhb_cetcocean_target_hlxais_dt_df");
        consumerProperties.setProperty(PropertyKeyConst.AccessKey, "6f181321efd9449ba45d2c69796b17f5");
        consumerProperties.setProperty(PropertyKeyConst.SecretKey, "OopH5XhRhlZfCg/O7iFaWotHkLQ=");
        consumerProperties.setProperty(PropertyKeyConst.NAMESRV_ADDR, "http://mq.namesrv.paas.sgpt.gov:9876");
        consumerProperties.setProperty(PropertyKeyConst.InstanceName, "sgpt_sgzyc");
        //集群方式订阅
//        consumerProperties.put(PropertyKeyConst.MessageModel,PropertyValueConst.CLUSTERING);
        Consumer consumer = ONSFactory.createConsumer(consumerProperties);
        consumer.subscribe("taiji_ax_tianao_radar_fusion", "tianao_radar_fusion", new MessageListener(){

            @Override
            public Action consume(Message message, ConsumeContext consumeContext) {
                FusionRadar fusionRadar=JSONObject.parseObject(message.getBody(),FusionRadar.class);
                Date receviveTime =fusionRadar.getReceiveTime();
                try {
                    Date nowDate =new Date();
//                    log.info("时间:"+receviveTime);
//                    log.info("时间1:"+nowDate);
                    log.info("时间差:"+(nowDate.getTime()-receviveTime.getTime())/1000.0);
                }catch (Exception e){
                    e.printStackTrace();
                }

                return Action.CommitMessage;
            }
        });
        consumer.start();
        System.out.println("Dynamic Consumer start success.");
    }

}
