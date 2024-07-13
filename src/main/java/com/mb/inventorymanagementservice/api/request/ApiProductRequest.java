package com.mb.inventorymanagementservice.api.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mb.inventorymanagementservice.config.money.MoneyDeserializer;
import com.mb.inventorymanagementservice.config.money.MoneySerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.javamoney.moneta.Money;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiProductRequest {

    @Schema(description = "Product name", example = "IPHONE 14")
    private String name;

    @Schema(description = "Product code", example = "IPHONE_14")
    private String productCode;

    @Schema(description = "Product description", example = "IPHONE 14 description")
    private String description;

    @JsonSerialize(using = MoneySerializer.class)
    @JsonDeserialize(using = MoneyDeserializer.class)
    @Schema(description = "Product price", example = """
            {
              "amount": "10.32",
              "currency": "EUR"
            }
            """)
    private Money price;

    @Schema(description = "Product quantity", example = "100")
    private int quantity;
}
