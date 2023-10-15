package com.project.snsserver.domain.board.repository.jpa.custom;

import com.project.snsserver.domain.board.model.dto.PostImageResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.project.snsserver.domain.board.model.entity.QPost.post;
import static com.project.snsserver.domain.board.model.entity.QPostImage.postImage;
import static com.querydsl.core.types.Projections.bean;
import static com.querydsl.jpa.JPAExpressions.select;

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
				bean(PostImageResponse.class,
					postImage.id.as("postImageId"),
					postImage.postImageUrl.as("postImageUrl"),
					postImage.createdAt.as("createdAt")
				)
			)
			.from(postImage)
			.where(postImage.post.id.eq(postId))
			.fetch();
	}

	@Override
	public Long deleteAllPostImageInPostIdsByMemberId(Long memberId) {
		return queryFactory.delete(postImage)
			.where(postImage.post.id.in(
					select(post.id).from(post).where(post.member.id.eq(memberId))
				)
			)
			.execute();
	}
}
