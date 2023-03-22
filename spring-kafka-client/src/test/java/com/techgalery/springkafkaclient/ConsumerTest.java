package com.techgalery.springkafkaclient;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Interaction;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest
@PactTestFor(providerName = "pactflow-example-provider-java-kafka")
public class ConsumerTest {

    @Pact(consumer = "pactflow-example-consumer-java-kafka")
    V4Pact createPactAsync(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody();
        body.stringType("name", "abcd123");

        return builder.expectsToReceive("a test message")
//                .withMetadata(md -> {
//            md.add("Content-Type", "application/json");
//            md.add("kafka_topic", "products");
//            md.add("test", "test");
//        })
                .withContent(body).toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createPactAsync", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V4)
    void test(V4Interaction.AsynchronousMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        var response = message.getContents().getContents().valueAsString();
        System.out.println("Message received -> " + response);

        assertDoesNotThrow(() -> {
            MyMessageHandler.getResponse(response);
        });
    }

}
