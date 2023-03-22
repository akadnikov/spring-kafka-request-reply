package com.techgalery.springkafkaserver;

import com.techgalery.model.Product;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

public class MyMessageBuilder {

    public static Message<Product> createResponse(Product request) {
        String reversedString = new StringBuilder(String.valueOf(request.getName())).reverse().toString();
        request.setName(reversedString);
        return MessageBuilder.withPayload(request).build();
    }
}
