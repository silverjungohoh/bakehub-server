package com.project.snsserver.domain.board.repository.jpa.custom;

import static com.project.snsserver.domain.board.model.entity.QComment.*;
import static com.project.snsserver.domain.board.model.entity.QPost.*;
import static com.project.snsserver.domain.member.model.entity.QMember.*;
import static com.querydsl.core.types.Projections.*;
import static com.querydsl.core.types.dsl.Expressions.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.project.snsserver.domain.board.model.dto.response.CommentResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

	private final JPAQueryFactory queryFactory;

	/**
	 * 특정 게시물의 댓글 조회
	 */
	@Override
	public Slice<CommentResponse> findAllCommentByPostId(Long postId, Long lastCommentId, String email,
		Pageable pageable) {

		List<CommentResponse> comments = queryFactory.select(
				fields(CommentResponse.class,
					comment.id.as("commentId"),
					comment.content.as("content"),
					member.nickname.as("nickname"),
					asBoolean(member.email.eq(email)).as("isWriter"),
					comment.createdAt.as("createdAt")
				)
			)
			.from(comment)
			.leftJoin(comment.post, post)
			.leftJoin(comment.member, member)
			.where(lastCommentId(lastCommentId), comment.post.id.eq(postId))
			.orderBy(comment.createdAt.desc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		return checkLastPage(pageable, comments);
	}

	/**
	 * 특정 게시물의 댓글 전체 삭제
	 */
	@Override
	public Long deleteAllCommentByPostId(Long postId) {
		return queryFactory.delete(comment)
			.where(comment.post.id.eq(postId))
			.execute();
	}

	private BooleanExpression lastCommentId(Long lastCommentId) {
		if (lastCommentId == null) {
			return null;
		}
		return comment.id.lt(lastCommentId);
	}

	private Slice<CommentResponse> checkLastPage(Pageable pageable, List<CommentResponse> comments) {
		boolean hasNext = false;

		// 조회한 댓글의 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음
		if (comments.size() > pageable.getPageSize()) {
			hasNext = true;
			comments.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(comments, pageable, hasNext);
	}
}
