package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.board.model.dto.PostHeartResponse;
import com.project.snsserver.domain.member.model.entity.Member;

public interface PostHeartService {

	/**
	 * 게시물에 좋아요 등록
	 */
	void pushHeart(Long postId, Member member);

	/**
	 * 게시물에 좋아요 취소
	 */
	void cancelHeart(Long postId, Long postHeartId, Member member);

	/**
	 * 게시물의 좋아요 개수 조회
	 */
	PostHeartResponse getPostHeartCountByPost(Long postId);
}
