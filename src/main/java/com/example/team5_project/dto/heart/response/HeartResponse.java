package com.example.team5_project.dto.heart.response;

import com.example.team5_project.dto.product.response.ReadProductResponse;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HeartResponse {

    private final Long id;
    private final Long memberId;
    private final ReadProductResponse product;
    private final LocalDateTime createdAt;

}
