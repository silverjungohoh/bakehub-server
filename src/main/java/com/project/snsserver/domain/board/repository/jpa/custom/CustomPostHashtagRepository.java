package com.project.snsserver.domain.board.repository.jpa.custom;

import com.project.snsserver.domain.board.model.dto.PostHashtagResponse;

import java.util.List;

public interface CustomPostHashtagRepository {

	List<PostHashtagResponse> findAllPostHashtagByPostId(Long postId);

	Long deletePostHashtagAllByPostId(Long postId);

	Long deletePostHashtagAllInPostIdsByMemberId(Long memberId);
}
