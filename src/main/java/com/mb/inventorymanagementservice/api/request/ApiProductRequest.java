package com.mb.inventorymanagementservice.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiProductRequest {

    @Schema(description = "Product name", example = "IPHONE 14")
    private String name;

    @Schema(description = "Product description", example = "IPHONE_14")
    private String productCode;

    @Schema(description = "Product description", example = "IPHONE 14 description")
    private String description;

    @Schema(description = "Product current price", example = "10.32")
    private BigDecimal currentPrice;

    @Schema(description = "Product currency", example = "EUR")
    private String currency;

    @Schema(description = "Product quantity", example = "100")
    private int quantity;
}
