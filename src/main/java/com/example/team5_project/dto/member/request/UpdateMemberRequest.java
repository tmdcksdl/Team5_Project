package com.example.team5_project.dto.member.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateMemberRequest {

    private String name;

    private String address;
}
