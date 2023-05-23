package com.example.demo.controller.kafka;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.RadarData;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
//@Component
public class KafkaCount {

    @Autowired
    protected StringRedisTemplate redisTemplate;

//    @KafkaListener(
//            topics = "${taiji.kafka.consumer.count_topic.topic}",
//            groupId = "${taiji.kafka.consumer.count_topic.group}")
    public void shipWarningRecord(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws ParseException {
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            redisTemplate.opsForValue().set(String.valueOf(UUID.randomUUID()), String.valueOf(msg),1, TimeUnit.SECONDS);
        }
        ack.acknowledge();
    }

}
