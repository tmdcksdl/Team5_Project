package com.example.team5_project.controller;

import com.example.team5_project.common.aspect.AuthCheck;
import com.example.team5_project.dto.member.request.SignInMemberRequest;
import com.example.team5_project.dto.member.request.SignUpMemberRequest;
import com.example.team5_project.dto.member.request.UpdateMemberRequest;
import com.example.team5_project.dto.member.response.FindMemberResponse;
import com.example.team5_project.dto.member.response.SignInMemberResponse;
import com.example.team5_project.dto.member.response.SignUpMemberResponse;
import com.example.team5_project.dto.member.response.UpdateMemberResponse;
import com.example.team5_project.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 회원 정보 조회
    @AuthCheck({"OWNER", "USER"})
    @GetMapping("/{memberId}")
    public ResponseEntity<FindMemberResponse> findMember(
            @PathVariable Long memberId,
            HttpServletRequest requestDto
    ) {
        FindMemberResponse findMemberResponse = memberService.findMember(memberId, requestDto);

        return new ResponseEntity<>(findMemberResponse, HttpStatus.OK);
    }

    // 회원 정보 수정
    @AuthCheck({"OWNER", "USER"})
    @PatchMapping("/{memberId}")
    public ResponseEntity<UpdateMemberResponse> updateMember(
            @PathVariable Long memberId,
            @RequestBody UpdateMemberRequest requestDto,
            HttpServletRequest servletRequest
    ) {
        UpdateMemberResponse updateMemberResponse = memberService.updateMember(memberId, requestDto, servletRequest);

        return new ResponseEntity<>(updateMemberResponse, HttpStatus.OK);
    }

    // 회원 탈퇴
    @AuthCheck({"OWNER", "USER"})
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable Long memberId,
            HttpServletRequest servletRequest
    ) {
        memberService.deleteMember(memberId, servletRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
