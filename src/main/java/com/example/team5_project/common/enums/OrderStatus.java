package com.example.team5_project.common.enums;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public enum OrderStatus {

    주문접수, 주문취소, 배송준비, 배송중, 배송완료;

    public static OrderStatus of(String status) {

        return Arrays.stream(OrderStatus.values())  //이넘의 값을 뽑는다
                .filter(r -> r.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("등록되지 않은 주문상태입니다"));
    }
}
