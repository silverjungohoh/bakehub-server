package com.project.snsserver.domain.member.repository.jpa;

import com.project.snsserver.domain.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
