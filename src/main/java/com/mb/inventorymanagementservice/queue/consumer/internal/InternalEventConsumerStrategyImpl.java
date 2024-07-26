package com.mb.inventorymanagementservice.queue.consumer.internal;

import com.mb.inventorymanagementservice.queue.event.Event;
import com.mb.inventorymanagementservice.queue.event.InventoryManagementServiceEventType;
import com.mb.inventorymanagementservice.queue.consumer.ConsumerStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class InternalEventConsumerStrategyImpl implements ConsumerStrategy {

    @Override
    public void execute(Event event) {
        log.info("Received an event in internal event consumer strategy implementation. execute - event: {}", event);
    }

    @Override
    public boolean canExecute(Event event) {
        return InventoryManagementServiceEventType.INTERNAL_EVENT.equals(event.getEventType());
    }
}
