package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.board.model.dto.EditCommentRequest;
import com.project.snsserver.domain.board.model.dto.EditCommentResponse;
import com.project.snsserver.domain.member.model.entity.Member;

import java.util.Map;

public interface CommentService {

    /**
     * 댓글 작성
     */
    EditCommentResponse writeComment(Long postId, EditCommentRequest request, Member member);

    /**
     * 댓글 작성
     */
    Map<String, String> deleteComment(Long postId, Long commentId, Member member);
}
