package com.project.snsserver.domain.member.repository.redis;

import com.project.snsserver.domain.member.model.entity.RefreshToken;

import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
