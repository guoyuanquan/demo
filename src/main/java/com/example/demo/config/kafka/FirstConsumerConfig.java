//package com.example.demo.config.kafka;
//
//
//import org.apache.kafka.clients.CommonClientConfigs;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.config.SaslConfigs;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @Author：guoyq
// * @name：consumer
// * @Date：2023/7/25 17:08
// * @Filename：consumer
// * kafka多数据源时配置文件
// */
//@EnableKafka
//@Configuration
//public class FirstConsumerConfig {
//    private String bootstarpServer;
//    private String enableAutoCommit;
//    private String kafkaProducerSASLJaasConfig;
//    private String kafkaSASLMechanism;
//    private String kafkaSecurityProtocol;
//
//    //消费端
//    @Bean
//    public Map<String,Object> FirstConsumerConfig(){
//        Map<String,Object> props=new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstarpServer);
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        return props;
//    }
//
//    @Bean
//    public ConsumerFactory<String,String> firstConsumerFactory(){
//        return new DefaultKafkaConsumerFactory<>(FirstConsumerConfig());
//    }
//
//    @Bean(name = "FirstConsumerFactory")
//    public ConcurrentKafkaListenerContainerFactory<Object, Object> ztptListener(){
//        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(FirstConsumerConfig()));
//        //并发量设置
//        factory.setConcurrency(5);
//        factory.getContainerProperties().setPollTimeout(3000);
//        return factory;
//    }
//
//
//
//    //生产端
//    @Bean
//    public Map<String, Object> firstProducerConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstarpServer);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaSecurityProtocol);
//        props.put(SaslConfigs.SASL_MECHANISM, kafkaSASLMechanism);
//        props.put("sasl.jaas.config", kafkaProducerSASLJaasConfig);
//        return props;
//    }
//
//    @Bean
//    public ProducerFactory<String, String> firstProducerFactory() {
//        return new DefaultKafkaProducerFactory<>(firstProducerConfigs());
//    }
//
//    @Bean(name = "firstKafkaProducer")
//    public KafkaTemplate<String, String> mainKafkaTemplate() {
//        return new KafkaTemplate<>(firstProducerFactory());
//    }
//}
