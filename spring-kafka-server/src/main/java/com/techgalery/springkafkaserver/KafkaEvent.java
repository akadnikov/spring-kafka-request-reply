package com.techgalery.springkafkaserver;

import com.techgalery.model.Product;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class KafkaEvent {
    // KafkaListener echoes the correlation ID and determines the reply topic
    @SneakyThrows
    @KafkaListener(groupId="${myproject.consumer-group}", topics = "${myproject.send-topics}", containerFactory = "kafkaListenerContainerFactory")
    @SendTo
    public Product listen(ConsumerRecord<String, Product> consumerRecord) {
        return MyMessageBuilder.createResponse(consumerRecord.value()).getPayload();
    }
}
