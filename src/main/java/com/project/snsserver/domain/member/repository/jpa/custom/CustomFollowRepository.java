package com.project.snsserver.domain.member.repository.jpa.custom;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.project.snsserver.domain.member.model.dto.response.FollowResponse;

public interface CustomFollowRepository {

	/**
	 * 나의 팔로잉 회원 목록 조회
	 */
	Slice<FollowResponse> findAllFollowingByMemberId(Long memberId, Long lastId, Pageable pageable);

	/**
	 * 나의 팔로워 회원 목록 조회
	 */
	Slice<FollowResponse> findAllFollowerByMemberId(Long memberId, Long lastId, Pageable pageable);
}

