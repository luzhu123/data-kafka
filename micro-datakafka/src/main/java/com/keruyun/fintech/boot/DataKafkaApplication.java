package com.keruyun.fintech.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 罗鹏飞
 * @version 1.0
 * @date 2018-01-29
 */

@SpringBootApplication(scanBasePackages = {"com.keruyun"}, exclude = {DataSourceAutoConfiguration.class, RedisAutoConfiguration.class,
        JmsAutoConfiguration.class, MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class})
@ImportResource(value = {"applicationContext-root.xml"})
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableAsync
@EnableAspectJAutoProxy
public class DataKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataKafkaApplication.class, args);
    }


}
