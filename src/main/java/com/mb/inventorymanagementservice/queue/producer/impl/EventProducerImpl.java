package com.mb.inventorymanagementservice.queue.producer.impl;

import com.mb.inventorymanagementservice.queue.event.Event;
import com.mb.inventorymanagementservice.queue.producer.EventProducer;
import com.mb.inventorymanagementservice.utils.Constants;
import com.mb.inventorymanagementservice.utils.TransactionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventProducerImpl implements EventProducer {

    private final StreamBridge streamBridge;

    @Override
    public void publishEvent(String bindingName, Event event) {
        Message<Event> message = MessageBuilder
                .withPayload(event)
                .setHeader(Constants.EVENT_TYPE_HEADER_KEY, event.getEventType())
                .build();

        TransactionUtils.onCommit(() -> {
            log.info("Publishing event in event producer. publishEvent - event: {}", event);
            streamBridge.send(bindingName, message);
        });
    }
}
