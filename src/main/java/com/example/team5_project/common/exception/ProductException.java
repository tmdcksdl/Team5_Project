package com.example.team5_project.common.exception;

import com.example.team5_project.common.exception.errorcode.ProductErrorCode;
import lombok.Getter;

@Getter
public class ProductException extends RuntimeException {
    private final ProductErrorCode errorCode;

    public ProductException(ProductErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
