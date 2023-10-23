package com.project.snsserver.domain.board.repository.jpa.custom;

import com.project.snsserver.domain.board.model.dto.response.PostDetailResponse;
import com.project.snsserver.domain.board.model.dto.response.PostResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomPostRepository {

	Slice<PostResponse> findAllPost(Long lastPostId, Pageable pageable);

	PostDetailResponse findPostByPostId(Long postId, Long memberId);

	Slice<PostResponse> findAllPostByHashtag(Long lastPostId, String name, Pageable pageable);

	Long deleteAllPostByMemberId(Long memberId);
}
