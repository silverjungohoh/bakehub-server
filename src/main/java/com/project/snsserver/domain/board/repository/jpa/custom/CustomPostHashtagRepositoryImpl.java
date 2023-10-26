package com.project.snsserver.domain.board.repository.jpa.custom;

import static com.project.snsserver.domain.board.model.entity.QHashtag.*;
import static com.project.snsserver.domain.board.model.entity.QPostHashtag.*;
import static com.querydsl.core.types.Projections.*;

import java.util.List;

import com.project.snsserver.domain.board.model.dto.response.PostHashtagResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

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
	public Long deleteAllPostHashtagByPostId(Long postId) {
		return queryFactory.delete(postHashtag)
			.where(postHashtag.post.id.eq(postId))
			.execute();
	}
}
