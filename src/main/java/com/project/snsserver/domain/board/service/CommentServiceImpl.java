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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.project.snsserver.global.error.type.BoardErrorCode.*;

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

    @Override
    @Transactional
    public Map<String, String> deleteComment(Long postId, Long commentId, Member member) {

        if(!postRepository.existsById(postId)) {
            throw new BoardException(POST_NOT_FOUND);
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BoardException(COMMENT_NOT_FOUND));

        if(!Objects.equals(member.getEmail(), comment.getMember().getEmail())) {
            throw new BoardException(FAIL_TO_DELETE_COMMENT);
        }

        commentRepository.delete(comment);
        return getMessage("댓글이 삭제되었습니다.");
    }

    private static Map<String, String> getMessage(String message) {
        Map<String, String> result = new HashMap<>();
        result.put("result", message);
        return result;
    }
}
