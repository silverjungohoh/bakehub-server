package com.project.snsserver.domain.board.service;

import static com.project.snsserver.domain.notification.type.NotificationType.*;
import static com.project.snsserver.global.error.type.BoardErrorCode.*;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.snsserver.domain.board.model.dto.response.PostHeartResponse;
import com.project.snsserver.domain.board.model.entity.Post;
import com.project.snsserver.domain.board.model.entity.PostHeart;
import com.project.snsserver.domain.board.repository.jpa.PostHeartRepository;
import com.project.snsserver.domain.board.repository.jpa.PostRepository;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.notification.model.dto.NotificationMessage;
import com.project.snsserver.domain.notification.handler.NotificationProducer;
import com.project.snsserver.global.error.exception.BoardException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostHeartServiceImpl implements PostHeartService {

	private final PostHeartRepository postHeartRepository;
	private final PostRepository postRepository;
	private final NotificationProducer notificationProducer;

	@Override
	@Transactional
	public void pushHeart(Long postId, Member member) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new BoardException(POST_NOT_FOUND));

		if (Objects.equals(member.getEmail(), post.getMember().getEmail())) {
			throw new BoardException(FAIL_TO_PUSH_HEART);
		}

		if (postHeartRepository.existsByPostAndMember(post, member)) {
			throw new BoardException(ALREADY_PUSH_HEART);
		}

		PostHeart heart = PostHeart.builder()
			.post(post)
			.member(member)
			.build();

		postHeartRepository.save(heart);

		NotificationMessage message = NotificationMessage.builder()
			.nickname(post.getMember().getNickname())
			.type(NEW_HEART)
			.targetId(postId)
			.build();

		notificationProducer.produce(message);
	}

	@Override
	@Transactional
	public void cancelHeart(Long postId, Long postHeartId, Member member) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new BoardException(POST_NOT_FOUND));

		PostHeart heart = postHeartRepository.findByPostAndMember(post, member)
			.orElseThrow(() -> new BoardException(POST_HEART_NOT_FOUND));

		postHeartRepository.delete(heart);
	}

	@Override
	public PostHeartResponse getPostHeartCountByPost(Long postId) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new BoardException(POST_NOT_FOUND));

		Long heartCnt = postHeartRepository.countByPost(post);

		return PostHeartResponse.from(post.getId(), heartCnt);
	}
}
