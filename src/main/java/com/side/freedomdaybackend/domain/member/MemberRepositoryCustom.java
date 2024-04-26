package com.side.freedomdaybackend.domain.member;

public interface MemberRepositoryCustom {
    public Member queryDslTest();

    public boolean existsMemberId(long id);

}
