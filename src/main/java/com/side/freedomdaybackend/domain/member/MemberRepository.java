package com.side.freedomdaybackend.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    boolean existsByEmail(String email);
    Optional<Member> findByEmailAndPassword(String email, String password);
    Optional<Member> findByEmail(String email);
}
