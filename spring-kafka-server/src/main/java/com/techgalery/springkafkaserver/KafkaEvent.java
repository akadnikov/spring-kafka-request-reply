package com.techgalery.springkafkaserver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaEvent {
    // KafkaListener echoes the correlation ID and determines the reply topic
    @SneakyThrows
    @KafkaListener(groupId="${myproject.consumer-group}", topics = "${myproject.send-topics}")
    @SendTo
    public Message<?> listen(ConsumerRecord<String, Object> consumerRecord) {
        String req = MyMessageHandler.getRequest(consumerRecord.value());
        return MyMessageBuilder.createResponse(req);
    }
}
