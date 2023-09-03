package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.board.model.dto.PostHeartResponse;
import com.project.snsserver.domain.board.model.entity.Post;
import com.project.snsserver.domain.board.model.entity.PostHeart;
import com.project.snsserver.domain.board.repository.jpa.PostHeartRepository;
import com.project.snsserver.domain.board.repository.jpa.PostRepository;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.notification.model.dto.NotificationMessage;
import com.project.snsserver.domain.notification.rabbitmq.NotificationProducer;
import com.project.snsserver.global.error.exception.BoardException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Objects;

import static com.project.snsserver.domain.notification.type.NotificationType.NEW_HEART;
import static com.project.snsserver.global.error.type.BoardErrorCode.*;

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
                .receiver(post.getMember().getEmail())
                .type(NEW_HEART)
                .content(String.format(NEW_HEART.getValue(), member.getNickname()))
                .createdAt(Timestamp.valueOf(heart.getCreatedAt()))
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
