package com.project.snsserver.domain.board.repository.jpa;

public interface CustomPostHeartRepository {

    Long deletePostHeartAllByPostId(Long postId);
}
