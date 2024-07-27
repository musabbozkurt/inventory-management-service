package com.mb.inventorymanagementservice.queue.event;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InternalEvent extends BaseEventDto implements Event {

    private UUID randomId;

    @Override
    public EventType getEventType() {
        return InventoryManagementServiceEventType.INTERNAL_EVENT;
    }
}
