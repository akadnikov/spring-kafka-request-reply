package com.techgalery.springkafkaserver;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

public class MyMessageBuilder {

    public static Message<String> createResponse(Object request) throws Exception {
        String reversedString = new StringBuilder(String.valueOf(request)).reverse().toString();
        return MessageBuilder.withPayload( "{\"name\" : \"" + reversedString + "\"}" ).build();
    }
}
