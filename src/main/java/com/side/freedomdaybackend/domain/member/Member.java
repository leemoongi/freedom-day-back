package com.side.freedomdaybackend.domain.member;

import com.side.freedomdaybackend.domain.loan.Loan;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "member")
    private final List<Loan> loanList = new ArrayList<>();

    private String email; // 이메일
    private String password; // 비밀번호
    private String nickName; // 닉네임
    private String birthDate; // 생년월일
    private Character sex; // 성별
    private LocalDateTime createDate; // 생성날짜
    private LocalDateTime modifyDate; // 수정날짜
    private Character status; // 0:정상  1:삭제  2:정지
}
