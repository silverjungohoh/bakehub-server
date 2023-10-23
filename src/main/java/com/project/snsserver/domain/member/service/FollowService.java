package com.project.snsserver.domain.member.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.project.snsserver.domain.member.model.dto.response.FollowResponse;
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

	/**
	 * 나의 팔로잉 목록 조회
	 */
	Slice<FollowResponse> getMyFollowingList(Member member, Long lastId, Pageable pageable);

	/**
	 * 나의 팔로워 목록 조회
	 */
	Slice<FollowResponse> getMyFollowerList(Member member, Long lastId, Pageable pageable);

	/**
	 * 특정 회원의 팔로잉 목록 조회
	 */
	Slice<FollowResponse> getFollowingList(Long memberId, Long lastId, Pageable pageable);

	/**
	 * 특정 회원의 팔로워 목록 조회
	 */
	Slice<FollowResponse> getFollowerList(Long memberId, Long lastFollowId, Pageable pageable);
}
