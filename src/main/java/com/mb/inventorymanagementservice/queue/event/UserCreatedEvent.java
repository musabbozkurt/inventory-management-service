package com.mb.inventorymanagementservice.queue.event;

import com.mb.inventorymanagementservice.data.entity.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserCreatedEvent extends BaseEventDto implements Event {

    private User user;

    @Override
    public EventType getEventType() {
        return InventoryManagementServiceEventType.USER_CREATED_EVENT;
    }
}
