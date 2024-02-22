package com.example.demo.controller.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author：guoyq
 * @name：Kafkaroducer
 * @Date：2024/1/30 17:00
 * @Filename：Kafkaroducer
 */
//@Component
@Slf4j
public class Kafkaroducer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic,String key, String jsonStr) {
        kafkaTemplate.send(topic,key, jsonStr)
                .addCallback(success -> {
                    if (Objects.nonNull(success)) {
                        RecordMetadata metadata = success.getRecordMetadata();
                        log.info("生产者成功发送数据到kafka！topic: {}, partition: {}, offset: {}",
                                metadata.topic(), metadata.partition(), metadata.offset());
                    }
                }, failure -> log.error("发送数据失败，失败原因为: {}", failure.getMessage()));
    }
}
