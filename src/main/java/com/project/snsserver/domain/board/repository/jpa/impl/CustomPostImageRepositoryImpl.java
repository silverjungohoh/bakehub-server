package com.project.snsserver.domain.board.repository.jpa.impl;

import com.project.snsserver.domain.board.repository.jpa.CustomPostImageRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.project.snsserver.domain.board.model.entity.QPostImage.postImage;

@RequiredArgsConstructor
public class CustomPostImageRepositoryImpl implements CustomPostImageRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Long deleteAllPostImageByPostId(Long postId) {
        return queryFactory.delete(postImage)
                .where(postImage.post.id.eq(postId))
                .execute();
    }
}
