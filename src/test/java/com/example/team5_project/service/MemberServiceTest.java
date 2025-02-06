package com.example.team5_project.service;

import com.example.team5_project.common.enums.Gender;
import com.example.team5_project.common.enums.UserType;
import com.example.team5_project.common.exception.MemberException;
import com.example.team5_project.common.utils.JwtUtil;
import com.example.team5_project.common.utils.PasswordEncoder;
import com.example.team5_project.dto.member.request.SignInMemberRequest;
import com.example.team5_project.dto.member.request.SignUpMemberRequest;
import com.example.team5_project.dto.member.response.SignInMemberResponse;
import com.example.team5_project.dto.member.response.SignUpMemberResponse;
import com.example.team5_project.entity.Member;
import com.example.team5_project.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtUtil jwtUtil;


        SignUpMemberRequest signInRequest = new SignUpMemberRequest("이수진", "email@naver.com",
                "passwordA13!", Gender.FEMALE.toString(), "내집", UserType.USER.toString()
        );

        String encodedPassword = "encodePassword1!";
        Member savedMember = Member.of(signInRequest, encodedPassword);



    @Test
    void 회원가입_성공(){
        ReflectionTestUtils.setField(savedMember, "id", 1L);

        // given
        when(memberRepository.findMemberByEmail(signInRequest.getEmail())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        // when
        SignUpMemberResponse actualResponse = memberService.signUpMember(signInRequest);

        // then
        assertThat(actualResponse.getId()).isEqualTo(savedMember.getId());
        assertThat(actualResponse.getEmail()).isEqualTo(savedMember.getEmail());
    }

    @Test
    void 회원가입_실패_이메일존재(){
        ReflectionTestUtils.setField(savedMember, "id", 1L);

        // given
        when(memberRepository.findMemberByEmail(signInRequest.getEmail())).thenReturn(Optional.of(savedMember));

        // then
        Assertions.assertThrows(MemberException.class,()->memberService.signUpMember(signInRequest));


    }

    @Test
    void 로그인_성공(){

        //given
        SignInMemberRequest request = new SignInMemberRequest("email@naver.com", "passwordA13!");
        Member foundMember = Member.of(signInRequest, encodedPassword);
        String token = "Bearer mfklgmlfdkgmdjkkldsdkdmcdkdpsfloekflsmlvmdks";

        //when?
        when(memberRepository.findMemberByEmail(any(String.class))).thenReturn(Optional.of(foundMember));

        when(passwordEncoder.matches(request.getPassword(), encodedPassword)).thenReturn(true);

        when(jwtUtil.generateToken(foundMember.getId(), foundMember.getName(),
                foundMember.getUserType())).thenReturn(token);

        SignInMemberResponse actualResponse = memberService.signInMember(request);

        //then
        assertThat(token).isEqualTo(actualResponse.getToken());

    }

    @Test
    void 로그인_실패_비밀번호불일치(){
        //given
        SignInMemberRequest request = new SignInMemberRequest("email@naver.com", "passwordA13!");
        Member foundMember = Member.of(signInRequest, encodedPassword);


        when(memberRepository.findMemberByEmail(request.getEmail())).thenReturn(Optional.of(foundMember));
        when(passwordEncoder.matches(request.getPassword(), foundMember.getPassword())).thenReturn(false);

        //then
       Assertions.assertThrows(MemberException.class, ()-> memberService.signInMember(request));

    }
}