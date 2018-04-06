package com.keruyun.fintech.commons.kafka;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${datakafka.consumer.servers}")
    private String servers;
    @Value("${datakafka.consumer.enable.auto.commit}")
    private boolean enableAutoCommit;
    @Value("${datakafka.consumer.session.timeout}")
    private String sessionTimeout;
    @Value("${datakafka.consumer.auto.commit.interval}")
    private String autoCommitInterval;
    @Value("${datakafka.consumer.group.id}")
    private String groupId;
    @Value("${datakafka.consumer.auto.offset.reset}")
    private String autoOffsetReset;
    @Value("${datakafka.consumer.concurrency}")
    private int concurrency;

    @Bean
    public DefaultKafkaConsumerFactory<String, String> dataConsumerFactory() {
        Map<String, Object> producerProperties = new HashMap<>();
        producerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        producerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        producerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        producerProperties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        producerProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,sessionTimeout);
        producerProperties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        producerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        producerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");

        DefaultKafkaConsumerFactory<String, String> factory = new DefaultKafkaConsumerFactory<>(producerProperties);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> dataKafkaListenerContainerFactory(
            @Qualifier("dataConsumerFactory") ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        //消费者的并行度在这里配置
        factory.setConcurrency(1);

        return factory;
    }

}