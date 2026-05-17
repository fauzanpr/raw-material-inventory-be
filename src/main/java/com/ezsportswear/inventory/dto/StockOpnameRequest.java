package com.ezsportswear.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockOpnameRequest {
    @NotNull(message = "Actual stock is required")
    @Min(value = 0, message = "Actual stock cannot be negative")
    private Integer actualStock;
}
