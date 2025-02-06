package com.example.team5_project.service;

import com.example.team5_project.common.exception.MemberException;
import com.example.team5_project.common.exception.errorcode.MemberErrorCode;
import com.example.team5_project.common.utils.JwtUtil;
import com.example.team5_project.common.utils.PasswordEncoder;
import com.example.team5_project.dto.member.request.SignInMemberRequest;
import com.example.team5_project.dto.member.request.SignUpMemberRequest;
import com.example.team5_project.dto.member.request.UpdateMemberRequest;
import com.example.team5_project.dto.member.response.FindMemberResponse;
import com.example.team5_project.dto.member.response.SignInMemberResponse;
import com.example.team5_project.dto.member.response.SignUpMemberResponse;
import com.example.team5_project.dto.member.response.UpdateMemberResponse;
import com.example.team5_project.entity.Member;
import com.example.team5_project.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입 서비스
    public SignUpMemberResponse signUpMember(SignUpMemberRequest requestDto) {
        // 등록된 이메일 여부 확인
        if (memberRepository.findMemberByEmail(requestDto.email()).isPresent()) {
            throw new MemberException(MemberErrorCode.EMAIL_EXIST);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.password());
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
        Member foundMember = memberRepository.findMemberByEmail(requestDto.email()).orElseThrow(() ->
                new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        if (!passwordEncoder.matches(requestDto.password(), foundMember.getPassword())) {
            throw new MemberException(MemberErrorCode.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(
                foundMember.getId(), foundMember.getName(),
                foundMember.getUserType()
        );

        return new SignInMemberResponse(token);
    }

    // 회원 정보 조회 서비스
    @Transactional(readOnly = true)
    public FindMemberResponse findMember(Long memberId, HttpServletRequest requestDto) {
        Long id = (Long) requestDto.getAttribute("id");

        Member foundMember = memberRepository.findById(memberId).orElseThrow(() ->
                new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        if (foundMember.getId() != id) {
            throw new MemberException(MemberErrorCode.ACCESS_DENIED);
        }

        return new FindMemberResponse(
                foundMember.getId(), foundMember.getName(),
                foundMember.getEmail(), foundMember.getGender(),
                foundMember.getAddress(), foundMember.getUserType(),
                foundMember.getCreatedAt(), foundMember.getUpdatedAt()
        );
    }

    // 회원 정보 수정 서비스
    public UpdateMemberResponse updateMember(
            Long memberId, UpdateMemberRequest requestDto,
            HttpServletRequest servletRequest
    ) {
        Long id = (Long) servletRequest.getAttribute("id");

        Member foundMember = memberRepository.findById(memberId).orElseThrow(() ->
                new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        if (foundMember.getId() != id) {
            throw new MemberException(MemberErrorCode.ACCESS_DENIED);
        }

        foundMember.updateMember(requestDto.name(), requestDto.address());

        return new UpdateMemberResponse(
            foundMember.getId(), foundMember.getName(),
            foundMember.getEmail(), foundMember.getGender(),
            foundMember.getAddress(), foundMember.getUserType(),
            foundMember.getCreatedAt(), foundMember.getUpdatedAt()
        );
    }

    // 회원 탈퇴 서비스
    public void deleteMember(Long memberId, HttpServletRequest servletRequest) {
        Long id = (Long) servletRequest.getAttribute("id");

        Member foundMember = memberRepository.findById(memberId).orElseThrow(() ->
                new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        if (foundMember.getId() != id) {
            throw new MemberException(MemberErrorCode.ACCESS_DENIED);
        }

        memberRepository.delete(foundMember);
    }
}
