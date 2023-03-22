package com.techgalery.springkafkaserver;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Interaction;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.techgalery.model.Product;
import org.apache.kafka.common.serialization.Deserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest
@PactTestFor(providerName = "kafka-client")
public class ConsumerSidePactTest {

    @Value("${myproject.send-topics}")
    private String SEND_TOPICS;

    @Pact(consumer = "kafka-server")
    V4Pact createPactAsync(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody();
        body.stringType("name", "abcd123");

        return builder.expectsToReceive("a test message")
//                .withMetadata(md -> {
//            md.add("Content-Type", "application/json");
//            md.add("kafka.replyTopic", SEND_TOPICS);
//            md.add("test", "test");
//        })
                .withContent(body).toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createPactAsync", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V4)
    void test(V4Interaction.AsynchronousMessage message) {
        var response = message.getContents().getContents().valueAsString();
        byte[] kafkaBytes = convertToKafkaBytes(message);

        System.out.println("Message received -> " + response);

        assertDoesNotThrow(() -> {
            expectApplicationToConsumeKafkaBytesSuccessfully(kafkaBytes);
        });
    }

    private byte[] convertToKafkaBytes(V4Interaction.AsynchronousMessage message) {
        return message.contentsAsBytes();
    }

    private void expectApplicationToConsumeKafkaBytesSuccessfully(byte[] kafkaBytes) {
        Product product = useProductionCodeToDeserializeKafkaBytesToDomain(kafkaBytes);
        MyMessageBuilder.createResponse(product);
//        ProductionCode productionCode = new ProductionCode();
//        productionCode.handle(consumerDomainRecord);
    }

    private Product useProductionCodeToDeserializeKafkaBytesToDomain(byte[] kafkaBytes) {
        Deserializer<Product> deserializer = getProductionKafkaDeserializer();
        return deserializer.deserialize("", kafkaBytes);
    }

    private Deserializer<Product> getProductionKafkaDeserializer() {
        var deserializer = new JsonDeserializer<>(Product.class);
        deserializer.addTrustedPackages("*");
        return deserializer;
    }

}
