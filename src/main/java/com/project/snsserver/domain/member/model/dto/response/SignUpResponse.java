package com.project.snsserver.domain.member.model.dto.response;

import com.project.snsserver.domain.member.model.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {

	private String nickname;

	private String profileImgUrl;

	public static SignUpResponse fromEntity(Member member) {
		return SignUpResponse.builder()
			.nickname(member.getNickname())
			.profileImgUrl(member.getProfileImgUrl())
			.build();
	}
}
