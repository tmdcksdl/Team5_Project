package com.example.team5_project.dto.member.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignInMemberRequest(

        @NotBlank(message = "이메일 입력은 필수입니다!")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
                , message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @Size(min = 8, max = 15, message = "비밀번호는 최소 8자 이상, 최대 15자 이하여야합니다.")
        String password
) {

}
