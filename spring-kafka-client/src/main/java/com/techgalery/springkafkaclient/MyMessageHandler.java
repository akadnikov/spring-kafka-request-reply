package com.techgalery.springkafkaclient;

public class MyMessageHandler {

    public static String getResponse(Object response) throws Exception {
        return response.toString();
    }
}
