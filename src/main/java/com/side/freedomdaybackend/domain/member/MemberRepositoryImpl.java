package com.side.freedomdaybackend.domain.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.side.freedomdaybackend.domain.member.QMember.*;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Member queryDslTest() {
        return queryFactory
                .selectFrom(member)
                .where(member.id.eq(1L))
                .fetchOne();
    }

    @Override
    public boolean existsMemberId(long id) {
        Member firstMember = queryFactory
                .selectFrom(member)
                .where(member.id.eq(id))
                .fetchFirst();

        return firstMember != null;
    }

}
