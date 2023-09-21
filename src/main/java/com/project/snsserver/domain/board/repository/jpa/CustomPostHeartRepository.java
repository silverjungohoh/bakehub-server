package com.project.snsserver.domain.board.repository.jpa;

public interface CustomPostHeartRepository {

	Long deletePostHeartAllByPostId(Long postId);

	Long deletePostHeartAllByMemberId(Long memberId);

	Long deletePostHeartAllInPostIdsByMemberId(Long memberId);
}
