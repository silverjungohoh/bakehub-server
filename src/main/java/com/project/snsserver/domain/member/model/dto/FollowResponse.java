package com.project.snsserver.domain.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowResponse {

	private Long followId;

	private String profileImgUrl;

	private String nickname;

	private String email;
}
