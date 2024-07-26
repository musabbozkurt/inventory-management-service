package com.mb.inventorymanagementservice.queue.producer;

import com.mb.inventorymanagementservice.queue.event.Event;

public interface EventProducer {

    void publishEvent(String bindingName, Event event);
}
