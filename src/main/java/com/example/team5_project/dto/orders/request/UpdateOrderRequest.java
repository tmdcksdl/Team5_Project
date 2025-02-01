package com.example.team5_project.dto.orders.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateOrderRequest(@NotBlank String orderStatus) {
}
