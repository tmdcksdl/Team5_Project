package com.example.team5_project.service;

import com.example.team5_project.common.utils.JwtUtil;
import com.example.team5_project.common.utils.PasswordEncoder;
import com.example.team5_project.dto.member.request.SignUpMemberRequest;
import com.example.team5_project.dto.member.response.SignUpMemberResponse;
import com.example.team5_project.entity.Member;
import com.example.team5_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입 서비스
    public SignUpMemberResponse signUpMember(SignUpMemberRequest requestDto) {

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Member member = Member.of(requestDto, encodedPassword);

        Member savedMember = memberRepository.save(member);

        return new SignUpMemberResponse(
                savedMember.getId(), savedMember.getName(),
                savedMember.getEmail(), savedMember.getGender(),
                savedMember.getAddress(), savedMember.getUserType(),
                savedMember.getCreatedAt()
        );
    }
}
