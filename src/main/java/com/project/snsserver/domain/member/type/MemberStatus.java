package com.project.snsserver.domain.member.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberStatus {

	ACTIVE("활동 계정"),
	WITHDRAWAL("탈퇴한 계정"),
	SUSPENDED("정지된 계정");

	private final String value;
}
