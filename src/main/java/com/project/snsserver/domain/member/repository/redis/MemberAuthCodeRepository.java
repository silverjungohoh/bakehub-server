package com.project.snsserver.domain.member.repository.redis;

import com.project.snsserver.domain.member.model.entity.MemberAuthCode;

import org.springframework.data.repository.CrudRepository;

public interface MemberAuthCodeRepository extends CrudRepository<MemberAuthCode, String> {
}
