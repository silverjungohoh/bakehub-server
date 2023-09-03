package com.project.snsserver.domain.board.repository.jpa.impl;

import com.project.snsserver.domain.board.repository.jpa.CustomPostHeartRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.project.snsserver.domain.board.model.entity.QPostHeart.postHeart;

@RequiredArgsConstructor
public class CustomPostHeartRepositoryImpl implements CustomPostHeartRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long deletePostHeartAllByPostId(Long postId) {
        return queryFactory.delete(postHeart)
                .where(postHeart.post.id.eq(postId))
                .execute();
    }
}
