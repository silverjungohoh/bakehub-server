package com.project.snsserver.domain.board.repository.jpa.impl;

import com.project.snsserver.domain.board.repository.jpa.CustomPostHeartRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import static com.project.snsserver.domain.board.model.entity.QPost.post;
import static com.project.snsserver.domain.board.model.entity.QPostHeart.postHeart;
import static com.querydsl.jpa.JPAExpressions.select;

@RequiredArgsConstructor
public class CustomPostHeartRepositoryImpl implements CustomPostHeartRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Long deletePostHeartAllByPostId(Long postId) {
		return queryFactory.delete(postHeart)
			.where(postHeart.post.id.eq(postId))
			.execute();
	}

	@Override
	public Long deletePostHeartAllByMemberId(Long memberId) {
		return queryFactory.delete(postHeart)
			.where(postHeart.member.id.eq(memberId))
			.execute();
	}

	public Long deletePostHeartAllInPostIdsByMemberId(Long memberId) {
		return queryFactory.delete(postHeart)
			.where(postHeart.post.id.in(select(post.id).from(post).where(post.member.id.eq(memberId))))
			.execute();
	}
}
