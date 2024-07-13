package com.mb.inventorymanagementservice.api.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mb.inventorymanagementservice.config.money.MoneyDeserializer;
import com.mb.inventorymanagementservice.config.money.MoneySerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.javamoney.moneta.Money;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiProductResponse {

    @Schema(description = "Product id")
    private Long id;

    @Schema(description = "Product creation date")
    private OffsetDateTime createdDateTime;

    @Schema(description = "Product modified date")
    private OffsetDateTime modifiedDateTime;

    @Schema(description = "Product name")
    private String name;

    @Schema(description = "Product code")
    private String productCode;

    @Schema(description = "Product description")
    private String description;

    @Schema(description = "Product price")
    @JsonSerialize(using = MoneySerializer.class)
    @JsonDeserialize(using = MoneyDeserializer.class)
    private Money price;

    @Schema(description = "Product quantity")
    private int quantity;
}
