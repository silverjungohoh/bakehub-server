package com.project.snsserver.domain.member.service;

import java.util.Map;

import com.project.snsserver.domain.member.model.entity.Member;

public interface FollowService {

	/**
	 * 회원 팔로우
	 */
	Map<String, String> follow(String nickname, Member follower);

	/**
	 * 회원 팔로우 취소
	 */
	void unfollow(String nickname, Member member);
}
