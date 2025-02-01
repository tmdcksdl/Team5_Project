package com.example.team5_project.dto.orders.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(@NotNull Integer quantity) {
}
