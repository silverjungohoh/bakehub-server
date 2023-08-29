package com.project.snsserver.domain.member.model.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@Builder
@RedisHash("AuthCode")
public class MemberAuthCode {

    @Id
    private String id; // auth code

    private String email;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiration;
}
