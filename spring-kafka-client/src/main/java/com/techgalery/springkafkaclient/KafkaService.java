package com.techgalery.springkafkaclient;

import com.techgalery.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class KafkaService {

    @Autowired
    private ReplyingKafkaTemplate<String, Product, Product> template;

    @Value("${myproject.send-topics}")
    private String SEND_TOPICS;

    public Product kafkaRequestReply(Product request) throws Exception {
        ProducerRecord<String, Product> record = new ProducerRecord<>(SEND_TOPICS, MyMessageBuilder.createRequest(request));
        RequestReplyFuture<String, Product, Product> replyFuture = template.sendAndReceive(record);
        SendResult<String, Product> sendResult = replyFuture.getSendFuture().get(60, TimeUnit.SECONDS);
        ConsumerRecord<String, Product> consumerRecord = replyFuture.get(60, TimeUnit.SECONDS);
        return consumerRecord.value();
    }
}
