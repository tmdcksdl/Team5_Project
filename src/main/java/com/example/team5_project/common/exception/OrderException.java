package com.example.team5_project.common.exception;

import com.example.team5_project.common.exception.errorcode.OrderErrorCode;
import lombok.Getter;

@Getter
public class OrderException extends RuntimeException {
    private final OrderErrorCode errorCode;

    public OrderException(OrderErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
