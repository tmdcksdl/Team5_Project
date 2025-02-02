package com.example.team5_project.dto.member.response;

import com.example.team5_project.common.enums.Gender;
import com.example.team5_project.common.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FindMemberResponse {

    private Long id;
    private String name;
    private String email;
    private Gender gender;
    private String address;
    private UserType user_type;
    private LocalDateTime create_at;
    private LocalDateTime updated_at;
}
