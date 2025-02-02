package com.example.team5_project.controller;

import com.example.team5_project.dto.member.request.SignUpMemberRequest;
import com.example.team5_project.dto.member.response.SignUpMemberResponse;
import com.example.team5_project.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpMemberResponse> signUpMember(
            @Valid @RequestBody SignUpMemberRequest requestDto
    ) {
        SignUpMemberResponse signUpMemberResponse = memberService.signUpMember(requestDto);

        return new ResponseEntity<>(signUpMemberResponse, HttpStatus.CREATED);
    }
}
