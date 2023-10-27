package com.project.snsserver.domain.member.model.dto.response;

import java.time.LocalDateTime;

import com.project.snsserver.domain.member.type.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyDetailResponse {

	private Long memberId;

	private String email;

	private String nickname;

	private Gender gender;

	private String profileImgUrl;

	private LocalDateTime createdAt; // 가입일

	private Long totalPostCnt; // 작성한 게시물 개수

	private Long totalPostHeartCnt; // 좋아요 누른 게시물 개수

	private Long followingCnt;

	private Long followerCnt;
}
