package com.keruyun.fintech.datakafka.kafka;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DataFactoryConsumer {

    @Autowired
    private DataFactoryProducer  producer;

    @KafkaListener(id = "dataFactory_receiver", topics = "${datakafka.producer.topic}",
            containerFactory = "dataKafkaListenerContainerFactory")
    public void onMessage(ConsumerRecord<String, String> record) throws Exception{
        System.out.println("收到消息");
        String message = record.value();
        System.out.println("消息为 "+message);
        System.out.println("消息消费结束");
        Thread.sleep(4000);
       //在这执行往大数据发送消息
        producer.send(message);
    }


}
