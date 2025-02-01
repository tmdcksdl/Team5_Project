package com.example.team5_project.dto.orders.response;

import com.example.team5_project.common.enums.OrderStatus;
import com.example.team5_project.entity.Orders;
import java.time.LocalDateTime;


public record OrderResponse(Long id, Long memberId, Long productId, Integer quantity, Long totalPrice,
                            OrderStatus orderStatus, LocalDateTime createdAt, LocalDateTime updatedAt
) {

    public OrderResponse(Orders order) {
        this(
                order.getId(),
                order.getMember().getId(),
                order.getProduct().getId(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getOrderStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
