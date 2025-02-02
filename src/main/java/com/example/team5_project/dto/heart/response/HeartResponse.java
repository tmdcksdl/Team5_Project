package com.example.team5_project.dto.heart.response;

import com.example.team5_project.dto.product.response.ProductResponse;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HeartResponse {

    private final Long id;
    private final Long memberId;
    private final Long productId;
    private final ProductResponse product; //todo 임시로 dto 파일 만들었음
    private final LocalDateTime createdAt;

}
