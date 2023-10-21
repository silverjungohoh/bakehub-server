package com.project.snsserver.domain.board.repository.jpa.custom;

import com.project.snsserver.domain.board.model.dto.PostHashtagResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.project.snsserver.domain.board.model.entity.QHashtag.hashtag;
import static com.project.snsserver.domain.board.model.entity.QPost.post;
import static com.project.snsserver.domain.board.model.entity.QPostHashtag.postHashtag;
import static com.querydsl.core.types.Projections.*;
import static com.querydsl.jpa.JPAExpressions.select;

@RequiredArgsConstructor
public class CustomPostHashtagRepositoryImpl implements CustomPostHashtagRepository {

	private final JPAQueryFactory queryFactory;

	public List<PostHashtagResponse> findAllPostHashtagByPostId(Long postId) {

		return queryFactory
			.select(
				fields(PostHashtagResponse.class,
					postHashtag.id.as("postHashtagId"),
					hashtag.name.as("tagName")
				)
			)
			.from(postHashtag)
			.leftJoin(postHashtag.hashtag, hashtag)
			.where(postHashtag.post.id.eq(postId))
			.fetch();
	}

	@Override
	public Long deletePostHashtagAllByPostId(Long postId) {
		return queryFactory.delete(postHashtag)
			.where(postHashtag.post.id.eq(postId))
			.execute();
	}

	@Override
	public Long deletePostHashtagAllInPostIdsByMemberId(Long memberId) {
		return queryFactory.delete(postHashtag)
			.where(postHashtag.post.id.in(
					select(post.id).from(post).where(post.member.id.eq(memberId))
				)
			)
			.execute();
	}
}
