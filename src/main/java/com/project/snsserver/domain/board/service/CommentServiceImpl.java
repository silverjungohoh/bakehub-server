package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.board.model.dto.EditCommentRequest;
import com.project.snsserver.domain.board.model.dto.EditCommentResponse;
import com.project.snsserver.domain.board.model.entity.Comment;
import com.project.snsserver.domain.board.model.entity.Post;
import com.project.snsserver.domain.board.repository.jpa.CommentRepository;
import com.project.snsserver.domain.board.repository.jpa.PostRepository;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.global.error.exception.BoardException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.snsserver.global.error.type.BoardErrorCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public EditCommentResponse writeComment(Long postId, EditCommentRequest request, Member member) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BoardException(POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .member(member)
                .build();

        commentRepository.save(comment);
        return EditCommentResponse.fromEntity(comment);
    }
}
