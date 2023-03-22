package com.techgalery.springkafkaclient;

import com.techgalery.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
class SpringKafkaClientApplicationTests {
	@Autowired KafkaService kafkaService;

	@Test
	void kafkaRequestReply_test() throws Exception {
		Product request = new Product();
		request.setName("name");
		String mustResponse = String.valueOf(request);
		Product sendReply = kafkaService.kafkaRequestReply(request);
		String responseString = String.valueOf(sendReply);
		assertEquals(mustResponse, responseString);
		log.info("Request message: {}, Response message: {}", request, responseString);
	}
}
