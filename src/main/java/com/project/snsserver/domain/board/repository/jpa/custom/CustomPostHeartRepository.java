package com.project.snsserver.domain.board.repository.jpa.custom;

public interface CustomPostHeartRepository {

	Long deletePostHeartAllByPostId(Long postId);

	Long deletePostHeartAllByMemberId(Long memberId);

	Long deletePostHeartAllInPostIdsByMemberId(Long memberId);
}
