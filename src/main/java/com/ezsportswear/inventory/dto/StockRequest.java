package com.ezsportswear.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockRequest {
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be greated than 0")
    private Integer quantity;
}
