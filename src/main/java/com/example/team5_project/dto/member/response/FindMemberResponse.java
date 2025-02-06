package com.example.team5_project.dto.member.response;

import com.example.team5_project.common.enums.Gender;
import com.example.team5_project.common.enums.UserType;

import java.time.LocalDateTime;

public record FindMemberResponse(
        Long id, String name,
        String email, Gender gender,
        String address, UserType userType,
        LocalDateTime createdAt, LocalDateTime updatedAt
) {

}
