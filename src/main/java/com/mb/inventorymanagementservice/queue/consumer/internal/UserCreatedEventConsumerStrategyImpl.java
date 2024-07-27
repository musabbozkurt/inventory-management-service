package com.mb.inventorymanagementservice.queue.consumer.internal;

import com.mb.inventorymanagementservice.queue.consumer.ConsumerStrategy;
import com.mb.inventorymanagementservice.queue.event.Event;
import com.mb.inventorymanagementservice.queue.event.InventoryManagementServiceEventType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class UserCreatedEventConsumerStrategyImpl implements ConsumerStrategy {

    @Override
    public void execute(Event event) {
        log.info("Received a user created event. execute - event: {}", event);
    }

    @Override
    public boolean canExecute(Event event) {
        return InventoryManagementServiceEventType.USER_CREATED_EVENT.equals(event.getEventType());
    }
}
