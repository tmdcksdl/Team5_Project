package com.example.team5_project.controller;

import com.example.team5_project.dto.member.request.SignInMemberRequest;
import com.example.team5_project.dto.member.request.SignUpMemberRequest;
import com.example.team5_project.dto.member.response.SignInMemberResponse;
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

    // 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<SignInMemberResponse> signInMember(
            @Valid @RequestBody SignInMemberRequest requestDto
    ) {
        SignInMemberResponse signInMemberResponse = memberService.signInMember(requestDto);

        return new ResponseEntity<>(signInMemberResponse, HttpStatus.OK);
    }
}
