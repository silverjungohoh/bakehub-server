package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.board.model.dto.EditCommentRequest;
import com.project.snsserver.domain.board.model.dto.EditCommentResponse;
import com.project.snsserver.domain.member.model.entity.Member;

public interface CommentService {

    /**
     * 댓글 작성
     */
    EditCommentResponse writeComment(Long postId, EditCommentRequest request, Member member);
}
