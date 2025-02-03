package com.example.team5_project.dto.orders.response;

import com.example.team5_project.entity.Orders;

public record UpdateOrderResponse(Long id, Long productId, String orderStatus) {

    public UpdateOrderResponse(Orders order){
        this(order.getId(), order.getProduct().getId(), order.getOrderStatus().name());
    }
}
