package com.example.team5_project.entity;

import com.example.team5_project.common.enums.Gender;
import com.example.team5_project.common.enums.UserType;
import com.example.team5_project.dto.member.request.SignUpMemberRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE member_id = ?")
public class Member extends BaseEntity {

    @Comment("회원 식별자")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Comment("회원명")
    @Column(nullable = false)
    private String name;

    @Comment("이메일")
    @Column(unique = true, nullable = false)
    private String email;

    @Comment("비밀번호")
    @Column(nullable = false)
    private String password;

    @Comment("성별")
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Comment("주소")
    @Column(nullable = false)
    private String address;

    @Comment("회원 타입")
    @Column(name = "user_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserType userType;


    public static Member of(SignUpMemberRequest requestDto, String encodedPassword) {
        return new Member(
                null, requestDto.getName(),
                requestDto.getEmail(), encodedPassword,
                Gender.of(requestDto.getGender()), requestDto.getAddress(),
                UserType.of(requestDto.getUser_type())
        );
    }

    public void updateMember(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
