package com.techgalery.springkafkaserver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyMessageHandler {

    public static String getRequest(Object request) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(request.toString());
        return String.valueOf(actualObj.get("name").textValue());
    }
}
