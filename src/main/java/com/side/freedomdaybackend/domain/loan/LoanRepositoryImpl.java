package com.side.freedomdaybackend.domain.loan;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.side.freedomdaybackend.domain.member.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoanRepositoryImpl implements LoanRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Member queryDslTest() {
        return null;
    }
}
