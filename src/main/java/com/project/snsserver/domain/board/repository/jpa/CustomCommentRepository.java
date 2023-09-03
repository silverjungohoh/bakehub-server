package com.project.snsserver.domain.board.repository.jpa;

import com.project.snsserver.domain.board.model.dto.CommentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface CustomCommentRepository {

    Slice<CommentResponse> findCommentAllByPostId(Long postId, Long lastCommentId, Pageable pageable);
}
