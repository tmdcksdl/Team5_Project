package com.example.team5_project.dto.orders.response;

import com.example.team5_project.common.enums.OrderStatus;

public record OrderPageableResponse(Long id, String memberName, String productName, OrderStatus orderStatus) {
}
