package com.project.snsserver.domain.board.repository.jpa.custom;

import com.project.snsserver.domain.board.model.dto.CommentResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomCommentRepository {

	Slice<CommentResponse> findCommentAllByPostId(Long postId, Long lastCommentId, String email, Pageable pageable);

	Long deleteCommentAllByPostId(Long postId);

	Long deleteCommentAllByMemberId(Long memberId);

	Long deleteCommentAllInPostIdsByMemberId(Long memberId);
}
