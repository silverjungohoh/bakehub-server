package com.project.snsserver.domain.board.repository.jpa.custom;

import com.project.snsserver.domain.board.model.dto.response.CommentResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomCommentRepository {

	Slice<CommentResponse> findAllCommentByPostId(Long postId, Long lastCommentId, String email, Pageable pageable);

	Long deleteAllCommentByPostId(Long postId);

	Long deleteAlCommentByMemberId(Long memberId);

	Long deleteAllCommentInPostIdsByMemberId(Long memberId);
}
