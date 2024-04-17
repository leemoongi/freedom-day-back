package com.side.freedomdaybackend.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    private String email; // 이메일
    private String password; // 비밀번호
    private String nickName; // 닉네임
    private Character sex; // 성별
    private String birthDate; // 생년월일
}
