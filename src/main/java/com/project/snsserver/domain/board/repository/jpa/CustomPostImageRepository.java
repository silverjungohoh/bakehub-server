package com.project.snsserver.domain.board.repository.jpa;

public interface CustomPostImageRepository {

    Long deleteAllPostImageByPostId(Long postId);
}
