package com.techgalery.springkafkaclient;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.TimeUnit;

public class MyMessageBuilder {

    public static Object createRequest(Object request) throws Exception {
        return request;
    }
}
