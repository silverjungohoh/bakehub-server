package com.project.snsserver.domain.board.repository.jpa.custom;

public interface CustomPostHeartRepository {

	Long deleteAllPostHeartByPostId(Long postId);
}
