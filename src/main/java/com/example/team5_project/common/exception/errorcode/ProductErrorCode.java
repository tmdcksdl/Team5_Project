package com.example.team5_project.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProductErrorCode {
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
    ALREADY_EXIST_PRODUCT(HttpStatus.CONFLICT, "이미 존재하는 상품입니다."),

    // 공통
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "올바르지 않은 입력값입니다.");

    private final HttpStatus status;
    private final String message;
}
