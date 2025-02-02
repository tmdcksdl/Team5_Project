package com.example.team5_project.service;

import com.example.team5_project.common.utils.JwtUtil;
import com.example.team5_project.common.utils.PasswordEncoder;
import com.example.team5_project.dto.member.request.SignInMemberRequest;
import com.example.team5_project.dto.member.request.SignUpMemberRequest;
import com.example.team5_project.dto.member.response.SignInMemberResponse;
import com.example.team5_project.dto.member.response.SignUpMemberResponse;
import com.example.team5_project.entity.Member;
import com.example.team5_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    // 로그인 서비스
    public SignInMemberResponse signInMember(SignInMemberRequest requestDto) {

        Member foundMember = memberRepository.findMemberByEmail(requestDto.getEmail()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 email을 가지고 있는 회원이 없습니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), foundMember.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(
                foundMember.getId(), foundMember.getName(),
                foundMember.getUserType()
        );

        return new SignInMemberResponse(token);
    }
}
