package com.project.snsserver.domain.board.repository.jpa.custom;

import static com.project.snsserver.domain.board.model.entity.QPostHeart.*;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomPostHeartRepositoryImpl implements CustomPostHeartRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Long deleteAllPostHeartByPostId(Long postId) {
		return queryFactory.delete(postHeart)
			.where(postHeart.post.id.eq(postId))
			.execute();
	}
}
