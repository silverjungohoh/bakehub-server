package com.project.snsserver.domain.board.repository.jpa;

import com.project.snsserver.domain.board.model.dto.PostImageResponse;

import java.util.List;

public interface CustomPostImageRepository {

    Long deleteAllPostImageByPostId(Long postId);

    List<PostImageResponse> findAllPostImageByPostId(Long postId);
}