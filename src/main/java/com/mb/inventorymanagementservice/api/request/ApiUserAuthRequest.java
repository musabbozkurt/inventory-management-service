package com.mb.inventorymanagementservice.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiUserAuthRequest {

    @NotNull
    private String username;

    @NotNull
    @ToString.Exclude
    private String password;
}
