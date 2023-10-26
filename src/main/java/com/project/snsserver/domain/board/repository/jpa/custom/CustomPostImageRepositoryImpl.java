package com.project.snsserver.domain.board.repository.jpa.custom;

import static com.project.snsserver.domain.board.model.entity.QPostImage.*;
import static com.querydsl.core.types.Projections.*;

import java.util.List;

import com.project.snsserver.domain.board.model.dto.response.PostImageResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomPostImageRepositoryImpl implements CustomPostImageRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Long deleteAllPostImageByPostId(Long postId) {
		return queryFactory.delete(postImage)
			.where(postImage.post.id.eq(postId))
			.execute();
	}

	@Override
	public List<PostImageResponse> findAllPostImageByPostId(Long postId) {
		return queryFactory.select(
				fields(PostImageResponse.class,
					postImage.id.as("postImageId"),
					postImage.postImageUrl.as("postImageUrl"),
					postImage.createdAt.as("createdAt")
				)
			)
			.from(postImage)
			.where(postImage.post.id.eq(postId))
			.fetch();
	}
}
