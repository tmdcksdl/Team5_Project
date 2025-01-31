package com.example.team5_project.common.enums;

import java.util.Arrays;

public enum Gender {

    MALE, FEMALE;

    public static Gender of(String gender) {
        return Arrays.stream(Gender.values())
                .filter(r -> r.name().equalsIgnoreCase(gender))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 권한입니다."));
    }
}
