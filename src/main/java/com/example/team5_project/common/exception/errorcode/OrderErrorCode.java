package com.example.team5_project.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorCode {
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),

    CANNOT_CANCEL(HttpStatus.BAD_REQUEST, "주문취소가 불가능합니다"),

    OUT_OF_STOCK(HttpStatus.NOT_FOUND, "품절된 상품입니다"),

    INSUFFICIENT_STOCK(HttpStatus.NOT_FOUND, "해당제품의 재고가 요청하신 수량보다 부족합니다"),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"접근 권한이 없습니다"),

    // 공통
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "올바르지 않은 입력값입니다.");



    private final HttpStatus status;
    private final String message;
}


