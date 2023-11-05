package com.project.snsserver.domain.board.service;

import static com.project.snsserver.domain.notification.type.NotificationType.*;
import static com.project.snsserver.global.error.type.BoardErrorCode.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.snsserver.domain.board.model.dto.request.EditCommentRequest;
import com.project.snsserver.domain.board.model.dto.response.CommentResponse;
import com.project.snsserver.domain.board.model.dto.response.EditCommentResponse;
import com.project.snsserver.domain.board.model.entity.Comment;
import com.project.snsserver.domain.board.model.entity.Post;
import com.project.snsserver.domain.board.repository.jpa.CommentRepository;
import com.project.snsserver.domain.board.repository.jpa.PostRepository;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.notification.model.dto.NotificationMessage;
import com.project.snsserver.domain.notification.handler.NotificationProducer;
import com.project.snsserver.global.error.exception.BoardException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final NotificationProducer notificationProducer;

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

		NotificationMessage message = NotificationMessage.builder()
			.nickname(post.getMember().getNickname())
			.type(NEW_COMMENT)
			.targetId(postId)
			.build();

		notificationProducer.produce(message);
		return EditCommentResponse.fromEntity(comment);
	}

	@Override
	@Transactional
	public Map<String, String> deleteComment(Long postId, Long commentId, Member member) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new BoardException(POST_NOT_FOUND));

		Comment comment = commentRepository.findByIdAndPost(commentId, post)
			.orElseThrow(() -> new BoardException(COMMENT_NOT_FOUND));

		if (!Objects.equals(member.getEmail(), comment.getMember().getEmail())) {
			throw new BoardException(FAIL_TO_DELETE_COMMENT);
		}

		commentRepository.delete(comment);
		return getMessage("댓글이 삭제되었습니다.");
	}

	@Override
	@Transactional
	public EditCommentResponse updateComment(Long postId, Long commentId, EditCommentRequest request, Member member) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new BoardException(POST_NOT_FOUND));

		Comment comment = commentRepository.findByIdAndPost(commentId, post)
			.orElseThrow(() -> new BoardException(COMMENT_NOT_FOUND));

		if (!Objects.equals(member.getEmail(), comment.getMember().getEmail())) {
			throw new BoardException(FAIL_TO_UPDATE_COMMENT);
		}

		comment.update(request.getContent());
		return EditCommentResponse.fromEntity(comment);
	}

	@Override
	@Transactional(readOnly = true)
	public Slice<CommentResponse> getCommentsByPost(Long postId, Long lastCommentId, String email, Pageable pageable) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new BoardException(POST_NOT_FOUND));

		return commentRepository.findAllCommentByPostId(post.getId(), lastCommentId, email, pageable);
	}

	private static Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		result.put("result", message);
		return result;
	}
}
