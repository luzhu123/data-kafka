package com.keruyun.fintech.datakafka.kafka;

import com.keruyun.fintech.commons.web.AbstractController;
import com.keruyun.fintech.commons.web.Response;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/kafka")
public class DataFactoryProducer extends AbstractController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${datakafka.producer.topic}")
    private String topic;

    private KafkaTemplate kafkaTemplate;

    @Autowired
    private ProducerListener<String,String> producerListener;

    @Autowired
    public void setKafkaTemplate(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplate.setProducerListener(producerListener);
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public Response sendKafka( @RequestBody JSONObject params) {
        try {
            String info = params.toString();
            kafkaTemplate.send(topic, "key", info);
            logger.info("发送kafka成功.");
            return new Response();
        } catch (Exception e) {
            logger.error("发送kafka失败", e);
            return new Response();
         }
    }

    public Response send( String info) {
        try {
            kafkaTemplate.send(topic, "key", info);
            logger.info("发送kafka成功.");
            return new Response();
        } catch (Exception e) {
            logger.error("发送kafka失败", e);
            return new Response();
        }
    }

}