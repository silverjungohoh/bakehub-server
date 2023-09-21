package com.project.snsserver.domain.member.repository.redis;

import com.project.snsserver.domain.member.model.entity.LogoutAccessToken;

import org.springframework.data.repository.CrudRepository;

public interface LogoutAccessTokenRepository extends CrudRepository<LogoutAccessToken, String> {
}
