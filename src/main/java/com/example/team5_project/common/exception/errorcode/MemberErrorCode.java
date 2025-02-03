package com.example.team5_project.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘못되었습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    USER_TYPE_MISSING(HttpStatus.FORBIDDEN, "사용자 권한 정보가 없습니다."),
    ONLY_USER_ACCESS(HttpStatus.FORBIDDEN,  "유저만 접근이 가능합니다."),
    ONLY_OWNER_ACCESS(HttpStatus.FORBIDDEN,  "사장님만 접근이 가능합니다."),
    EMAIL_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하거나 탈퇴한 이메일입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_FOUND_OWNER(HttpStatus.NOT_FOUND, "존재하지 않는 사장님입니다."),
    INVALID_USER_TYPE(HttpStatus.NOT_FOUND, "유효하지 않은 사용자 타입입니다"),

    // 공통
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "올바르지 않은 입력값입니다.");

    private final HttpStatus status;
    private final String message;

}
