package com.project.snsserver.domain.member.model.entity;

import lombok.Builder;
import lombok.Getter;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@Builder
@RedisHash("LogoutAccessToken")
public class LogoutAccessToken {

	@Id
	private String id; // access token

	private String email;

	@TimeToLive(unit = TimeUnit.MILLISECONDS)
	private Long expiration;
}
