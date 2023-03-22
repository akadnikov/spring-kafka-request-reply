package com.techgalery.springkafkaserver;

import au.com.dius.pact.core.model.Interaction;
import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.*;
import java.util.HashMap;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

@Provider("pactflow-example-provider-java-kafka")
@PactFolder("pact")
public class ProviderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderTest.class);

    Interaction interaction;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void testTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void before(Interaction interaction, PactVerificationContext context) {
        this.interaction = interaction;
        context.setTarget(new MessageTestTarget());
    }

    @SneakyThrows
    @PactVerifyProvider("a test message")
    public MessageAndMetadata messageRecievedAndCreated() {

        //var req = MyMessageHandler.getRequest(interaction.asSynchronousMessages().getRequest().getContents().valueAsString());
        Message<String> message = MyMessageBuilder.createResponse("abcd123");
        return generateMessageAndMetadata(message);
    }

    private MessageAndMetadata generateMessageAndMetadata(Message<String> message) {
        HashMap<String, Object> metadata = new HashMap<String, Object>();
        message.getHeaders().forEach((k, v) -> metadata.put(k, v));

        return new MessageAndMetadata(message.getPayload().getBytes(), metadata);
    }
}
