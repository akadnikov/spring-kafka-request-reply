package com.techgalery.springkafkaclient;

import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.*;

import java.util.Map;

import com.techgalery.model.Product;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider("kafka-client")
@PactFolder("pact")
public class ProviderSidePactTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderSidePactTest.class);
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String KEY_CONTENT_TYPE = "contentType";

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void testTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new MessageTestTarget());
    }

    @PactVerifyProvider("a test message")
    MessageAndMetadata verifySimpleMessageEvent() {
        Map<String, Object> metadata = Map.of(KEY_CONTENT_TYPE, JSON_CONTENT_TYPE);
        Product product = new Product();
        product.setName("name");
        JsonSerializer<Product> serializer = createProductionKafkaSerializer();
        byte[] bytes = serializer.serialize("", product);
        return createPactRepresentationFor(metadata, bytes);
    }

    private JsonSerializer<Product> createProductionKafkaSerializer() {
        Map<String, Object> config = Map.of();
        JsonSerializer<Product> jsonSerializer = new JsonSerializer<>();
        jsonSerializer.configure(config, false);
        return jsonSerializer;
    }

    private MessageAndMetadata createPactRepresentationFor(Map<String, Object> metadata, byte[] bytes) {
        return new MessageAndMetadata(bytes, metadata);
    }
}
