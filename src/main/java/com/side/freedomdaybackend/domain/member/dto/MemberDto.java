package com.side.freedomdaybackend.domain.member.dto;

import com.side.freedomdaybackend.domain.loan.Loan;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String email; // 이메일
    private String nickName; // 닉네임
    private Character sex; // 성별
    private LocalDateTime createDate; // 생성날짜
    private LocalDateTime modifyDate; // 수정날짜
    private Character status; // 0:정상  1:삭제  2:정지
}
