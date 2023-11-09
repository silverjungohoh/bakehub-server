package com.project.snsserver.domain.board.repository.jpa.custom;

import static com.project.snsserver.domain.board.model.entity.QComment.*;
import static com.project.snsserver.domain.board.model.entity.QHashtag.*;
import static com.project.snsserver.domain.board.model.entity.QPost.*;
import static com.project.snsserver.domain.board.model.entity.QPostHashtag.*;
import static com.project.snsserver.domain.board.model.entity.QPostHeart.*;
import static com.project.snsserver.domain.member.model.entity.QMember.*;
import static com.querydsl.core.types.ExpressionUtils.*;
import static com.querydsl.core.types.Projections.*;
import static com.querydsl.jpa.JPAExpressions.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.util.StringUtils;

import com.project.snsserver.domain.board.model.dto.response.PostDetailResponse;
import com.project.snsserver.domain.board.model.dto.response.PostResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<PostResponse> findAllPost(Long lastPostId, String keyword, Pageable pageable) {

		BooleanBuilder builder = new BooleanBuilder();
		builder.or(titleLike(keyword));
		builder.or(contentLike(keyword));

		List<PostResponse> posts = queryFactory
			.select(getPostResponseFields())
			.from(post)
			.leftJoin(post.member, member)
			.where(lastPostId(lastPostId))
			.where(builder)
			.limit(pageable.getPageSize() + 1)
			.orderBy(post.createdAt.desc())
			.fetch();

		return checkLastPage(pageable, posts);
	}

	@Override
	public PostDetailResponse findPostByPostId(Long postId, Long memberId) {
		return queryFactory
			.select(fields(PostDetailResponse.class,
					post.id.as("postId"),
					post.title.as("title"),
					post.content.as("content"),
					member.nickname.as("nickname"),
					post.createdAt.as("createdAt"),
					postHeart.isNotNull().as("hasHeart"),
					as(getCommentCount(), "commentCnt"),
					as(getHeartCount(), "heartCnt")
				)
			)
			.from(post)
			.leftJoin(post.member, member)
			.leftJoin(postHeart).on(postHeart.post.id.eq(postId), postHeart.member.id.eq(memberId))
			.where(post.id.eq(postId))
			.fetchOne();
	}

	@Override
	public Slice<PostResponse> findAllPostByHashtag(Long lastPostId, String name, Pageable pageable) {

		List<PostResponse> postsByHashtag = queryFactory
			.select(getPostResponseFields())
			.from(post)
			.innerJoin(post.member, member)
			.leftJoin(post.postHashtags, postHashtag)
			.leftJoin(postHashtag.hashtag, hashtag)
			.where(lastPostId(lastPostId), hashtag.name.eq(name))
			.limit(pageable.getPageSize() + 1)
			.orderBy(post.createdAt.desc())
			.fetch();

		return checkLastPage(pageable, postsByHashtag);
	}

	private BooleanExpression lastPostId(Long lastPostId) {
		if (lastPostId == null) {
			return null;
		}
		return post.id.lt(lastPostId);
	}

	private Slice<PostResponse> checkLastPage(Pageable pageable, List<PostResponse> posts) {
		boolean hasNext = false;

		// 조회한 게시물의 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음
		if (posts.size() > pageable.getPageSize()) {
			hasNext = true;
			posts.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(posts, pageable, hasNext);
	}

	private QBean<PostResponse> getPostResponseFields() {
		return fields(PostResponse.class,
			post.id.as("postId"),
			post.title.as("title"),
			post.content.as("content"),
			member.nickname.as("nickname"),
			post.createdAt.as("createdAt"),
			as(getCommentCount(), "commentCnt"),
			as(getHeartCount(), "heartCnt")
		);
	}

	// where 조건
	private BooleanExpression titleLike(String title) {
		return StringUtils.hasText(title) ? post.title.contains(title) : null;
	}

	// where 조건
	private BooleanExpression contentLike(String content) {
		return StringUtils.hasText(content) ? post.content.contains(content) : null;
	}


	private JPQLQuery<Long> getCommentCount() {
		return select(comment.id.count()).from(comment)
			.where(comment.post.id.eq(post.id));
	}

	private JPQLQuery<Long> getHeartCount() {
		return select(postHeart.id.count()).from(postHeart)
			.where(postHeart.post.id.eq(post.id));
	}
}
