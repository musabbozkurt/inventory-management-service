package com.mb.inventorymanagementservice.queue.consumer;

import com.mb.inventorymanagementservice.queue.event.Event;

public interface ConsumerStrategy {

    void execute(Event event);

    boolean canExecute(Event event);
}
