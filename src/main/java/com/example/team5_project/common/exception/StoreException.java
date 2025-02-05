package com.example.team5_project.common.exception;

import com.example.team5_project.common.exception.errorcode.StoreErrorCode;
import lombok.Getter;

@Getter
public class StoreException extends RuntimeException {
    private final StoreErrorCode errorCode;

    public StoreException(StoreErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
