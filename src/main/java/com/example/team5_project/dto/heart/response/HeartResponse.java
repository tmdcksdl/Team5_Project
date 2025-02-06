package com.example.team5_project.dto.heart.response;

import com.example.team5_project.dto.product.response.ReadProductResponse;

import java.time.LocalDateTime;

public record HeartResponse(
        Long id, Long memberId,
        ReadProductResponse product, LocalDateTime createdAt
) {

}
