package com.side.freedomdaybackend.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequestDto {

    private String email; // 이메일
    private String password;// 비밀번호
}
