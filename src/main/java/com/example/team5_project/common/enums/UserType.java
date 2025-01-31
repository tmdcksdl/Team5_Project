package com.example.team5_project.common.enums;

import java.util.Arrays;

public enum UserType {

    OWNER, USER;

    public static UserType of(String role) {
        return Arrays.stream(UserType.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 권한입니다."));
    }
}
