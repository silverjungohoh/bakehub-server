package com.project.snsserver.domain.member.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {

	GUEST("ROLE_GUEST"),
	USER("ROLE_USER"),
	ADMIN("ROLE_ADMIN");

	private final String value;
}
