package com.example.team5_project.common.exception;

import com.example.team5_project.common.exception.errorcode.MemberErrorCode;
import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {
    private final MemberErrorCode errorCode;

    public MemberException(MemberErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
