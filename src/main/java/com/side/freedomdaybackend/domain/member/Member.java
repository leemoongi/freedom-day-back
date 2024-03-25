package com.side.freedomdaybackend.domain.member;

import com.side.freedomdaybackend.domain.loan.Loan;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "member")
    private List<Loan> loanList = new ArrayList<>();

    private String email; // 이메일
    private String name; // 이름
    private String phoneNumber; //휴대폰 번호
    private String nickName; // 닉네임
    private String sex; // 성별
    private String createDate; // 생성날짜
    private String modifyDate; // 수정날짜
    private Boolean deleted; // 삭제 여부
}
