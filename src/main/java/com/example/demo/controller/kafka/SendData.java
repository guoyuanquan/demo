package com.example.demo.controller.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @Author：guoyq
 * @name：SendData
 * @Date：2024/1/30 17:06
 * @Filename：SendData
 */
//@Component
public class SendData {

    @Resource
    private Kafkaroducer kafkaProducer;

    @PostConstruct
    public void send(){
        String topic ="taiji_ax_bak_one";
        String key="1";
        String data="123";
        kafkaProducer.sendMessage(topic,key,data);
    }
}
