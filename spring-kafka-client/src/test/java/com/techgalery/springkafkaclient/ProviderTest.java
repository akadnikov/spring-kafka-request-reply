package com.techgalery.springkafkaclient;

import au.com.dius.pact.core.model.Interaction;
import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.techgalery.model.Product;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;

@Provider("pactflow-example-consumer-java-kafka")
@PactFolder("pact")
public class ProviderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderTest.class);

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void testTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void before(Interaction interaction, PactVerificationContext context) {
        context.setTarget(new MessageTestTarget());
    }

    @SneakyThrows
    @PactVerifyProvider("a test message")
    public MessageAndMetadata messageRecievedAndCreated() {

        Product request = new Product();
        request.setName("name");

        Message<Product> message = MessageBuilder.withPayload( MyMessageBuilder.createRequest(request)).build();
        return generateMessageAndMetadata(message);
    }

    private MessageAndMetadata generateMessageAndMetadata(Message<Product> message) {
        HashMap<String, Object> metadata = new HashMap<String, Object>();
        message.getHeaders().forEach((k, v) -> metadata.put(k, v));

        return new MessageAndMetadata(message.getPayload().toString().getBytes(), metadata);
    }
}
