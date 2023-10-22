package com.project.snsserver.domain.board.repository.jpa.custom;

public interface CustomPostHeartRepository {

	Long deleteAllPostHeartByPostId(Long postId);

	Long deleteAllPostHeartByMemberId(Long memberId);

	Long deleteAllPostHeartInPostIdsByMemberId(Long memberId);
}
