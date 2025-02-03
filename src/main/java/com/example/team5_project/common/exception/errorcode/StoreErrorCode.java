package com.example.team5_project.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StoreErrorCode {
    NOT_FOUND_STORE(HttpStatus.NOT_FOUND, "존재하지 않는 가게입니다."),

    // 공통
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "올바르지 않은 입력값입니다.");

    private final HttpStatus status;
    private final String message;
}
