package com.side.freedomdaybackend.domain.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email; // 이메일
    private String name; // 이름
    private String phoneNumber; //휴대폰 번호
    private String nickName; // 닉네임
    private String sex; // 성별
    private String createDate; // 생성날짜
    private String modifyDate; // 수정날짜
    private Boolean deleted; // 삭제 여부
}
