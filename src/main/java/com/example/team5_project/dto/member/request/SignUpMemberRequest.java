package com.example.team5_project.dto.member.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpMemberRequest {

    @NotBlank(message = "이름 입력은 필수입니다.")
    private String name;

    @NotBlank(message = "이메일 입력은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
            , message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$"
            , message = "대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자 포함해주세요.")
    @Size(min = 8, max = 15, message = "비밀번호는 최소 8글자 이상, 최대 15자 이하여야 합니다.")
    private String password;

    @NotBlank(message = "성별 입력은 필수입니다.")
    private String gender;

    @NotBlank(message = "주소 입력은 필수입니다.")
    private String address;

    @NotBlank(message = "OWNER(사장님), USER(회원) 권한 입력은 필수입니다.")
    private String user_type;
}
