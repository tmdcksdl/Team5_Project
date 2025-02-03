package com.example.team5_project.common.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<Map<String, String>> memberExceptionHandler(MemberException ex) {
        return exceptionHandler(ex.getMessage(), ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<Map<String, String>> orderExceptionHandler(OrderException ex) {
        return exceptionHandler(ex.getMessage(), ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<Map<String, String>> productExceptionHandler(ProductException ex) {
        return exceptionHandler(ex.getMessage(), ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(StoreException.class)
    public ResponseEntity<Map<String, String>> storeExceptionHandler(StoreException ex) {
        return exceptionHandler(ex.getMessage(), ex.getErrorCode().getStatus());
    }


    private ResponseEntity<Map<String, String>> exceptionHandler(String errorMessage, HttpStatus status) {

        Map<String, String> response = new LinkedHashMap<>();

        response.put("Error Message", errorMessage);
        log.info("에러 발생 >>> {}", errorMessage);
        return new ResponseEntity<>(response, status);
    }

}
