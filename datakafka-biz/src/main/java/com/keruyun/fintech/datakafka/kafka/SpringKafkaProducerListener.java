package com.keruyun.fintech.datakafka.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

/**
 * 
 * Description: 自定义一个Listener<br/>
 */
@Component
public class SpringKafkaProducerListener implements ProducerListener<String, String> {



	public void onSuccess(String topic, Integer partition, String key,
			String value, RecordMetadata recordMetadata) {
		System.out.println("已经发送:主题为[" + topic + "],分区为["
				+ recordMetadata.partition() + "],发送时间为["+recordMetadata.timestamp()+"],信息如下：");
		System.out.println(value);

	}

	public void onError(String topic, Integer partition, String key,
			String value, Exception e) {
		System.out.println("消息发送失败:topic:" + topic + ",value" + value
				+ ",exception:" + e.getLocalizedMessage());
	}

	/**
	 * 要onSuccess方法执行，需要返回true
	 */
	public boolean isInterestedInSuccess() {
		return true;
	}

}
