package com.techgalery.springkafkaclient;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.SynchronousMessagePactBuilder;
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
@PactTestFor(providerName = "pactflow-example-provider-java-kafka", providerType = ProviderType.SYNCH_MESSAGE, pactVersion = PactSpecVersion.V4)
public class PactTest {
    
    @Pact(consumer = "pactflow-example-consumer-java-kafka")
    V4Pact createPact(SynchronousMessagePactBuilder builder) {

        PactDslJsonBody requestBody = new PactDslJsonBody();
        requestBody.stringType("name", "abcd123");

        PactDslJsonBody responseBody = new PactDslJsonBody();
        responseBody.stringType("name", "321dcba");

        return builder.expectsToReceive("a test message")
                .withRequest(messageContentsBuilder -> messageContentsBuilder.withContent(requestBody))
                .withResponse(messageContentsBuilder -> messageContentsBuilder.withContent(responseBody))
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createPact", providerType = ProviderType.SYNCH_MESSAGE, pactVersion = PactSpecVersion.V4)
    void test(V4Interaction.SynchronousMessages message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        var request = message.getRequest().getContents().valueAsString();
        var response = message.getResponse().get(0).getContents().valueAsString();

        System.out.println("Message sended -> " + request);
        System.out.println("Message received -> " + response);

        assertDoesNotThrow(() -> {
            MyMessageBuilder.createRequest(request);
            MyMessageHandler.getResponse(response);
        });
    }


}
