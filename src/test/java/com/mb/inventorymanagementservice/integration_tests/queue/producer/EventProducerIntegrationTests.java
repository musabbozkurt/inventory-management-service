package com.mb.inventorymanagementservice.integration_tests.queue.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mb.inventorymanagementservice.base.BaseUnitTest;
import com.mb.inventorymanagementservice.queue.QueueChannels;
import com.mb.inventorymanagementservice.queue.event.InternalEvent;
import com.mb.inventorymanagementservice.queue.event.UserCreatedEvent;
import com.mb.inventorymanagementservice.queue.producer.EventProducer;
import com.mb.inventorymanagementservice.queue.producer.impl.EventProducerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test-containers")
public class EventProducerIntegrationTests extends BaseUnitTest {

    @Autowired
    private StreamBridge streamBridge;

    private EventProducer eventProducer;

    @BeforeEach
    void setUp() {
        eventProducer = new EventProducerImpl(streamBridge);
    }

    @Test
    void testPublishEvent() {
        InternalEvent actualInternalEvent = InternalEvent.builder()
                .randomId(UUID.randomUUID())
                .build();

        eventProducer.publishEvent(QueueChannels.INTERNAL_EVENT_PRODUCER, actualInternalEvent);
    }

    @Test
    @Disabled("Kafka must be running on Docker if this method needs to be enabled.")
    public void testPublishEvent_ShouldSucceed() throws IOException {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(TestChannelBinderConfiguration
                .getCompleteConfiguration(EmptyConfiguration.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            // Arrange
            String bindingName = "test-destination";
            InternalEvent expectedInternalEvent = InternalEvent.builder()
                    .randomId(UUID.randomUUID())
                    .build();
            StreamBridge streamBridge = context.getBean(StreamBridge.class);
            EventProducerImpl producer = new EventProducerImpl(streamBridge);
            ObjectMapper objectMapper = new ObjectMapper();

            // Act
            producer.publishEvent(bindingName, expectedInternalEvent);

            OutputDestination output = context.getBean(OutputDestination.class);
            Message<byte[]> result = output.receive(100, bindingName);

            // Assertions
            assertThat(result).isNotNull();
            InternalEvent actualEvent = objectMapper.readValue(result.getPayload(), InternalEvent.class);
            String actualEventAsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(actualEvent);
            assertThat(new String(result.getPayload())).isEqualTo(actualEventAsString);
            assertThat(actualEvent).isEqualTo(expectedInternalEvent);
        }
    }

    @Test
    void testPublishEvent_ShouldPublishUserCreatedEvent() {
        UserCreatedEvent actualUserCreatedEvent = UserCreatedEvent.builder()
                .user(getUser())
                .build();

        eventProducer.publishEvent(QueueChannels.USER_CREATED_EVENT_PRODUCER, actualUserCreatedEvent);
    }

    @EnableAutoConfiguration
    public static class EmptyConfiguration {
    }
}
