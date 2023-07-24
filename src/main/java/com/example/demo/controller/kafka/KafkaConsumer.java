package com.example.demo.controller.kafka;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.RadarData;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

//@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(
            topics = "${taiji.kafka.consumer.tianao_original.topic}",
            groupId = "${taiji.kafka.consumer.tianao_original.group}")
    public void shipWarningRecord(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws ParseException {
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            log.info("数据："+msg);
            ack.acknowledge();
        }
        ack.acknowledge();
    }
}