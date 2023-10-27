package com.project.snsserver.domain.member.repository.jpa.custom;

import com.project.snsserver.domain.member.model.dto.response.MemberDetailResponse;
import com.project.snsserver.domain.member.model.dto.response.MyDetailResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import static com.project.snsserver.domain.board.model.entity.QPost.post;
import static com.project.snsserver.domain.board.model.entity.QPostHeart.postHeart;
import static com.project.snsserver.domain.member.model.entity.QFollow.*;
import static com.project.snsserver.domain.member.model.entity.QMember.member;
import static com.querydsl.core.types.ExpressionUtils.as;
import static com.querydsl.core.types.Projections.*;
import static com.querydsl.jpa.JPAExpressions.*;

@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public MemberDetailResponse findMemberDetailByMemberId(Long myMemberId, Long memberId) {

		return queryFactory.select(
				fields(MemberDetailResponse.class,
					member.id.as("memberId"),
					member.email.as("email"),
					member.nickname.as("nickname"),
					member.gender.as("gender"),
					member.profileImgUrl.as("profileImgUrl"),
					member.createdAt.as("createdAt"),
					as(select(post.id.count()).from(post)
							.where(post.member.id.eq(memberId)),
						"totalPostCnt"),
					as(select(postHeart.id.count()).from(postHeart)
							.where(postHeart.member.id.eq(memberId)),
						"totalPostHeartCnt"),
					as(select(follow.id.count()).from(follow)
						.where(follow.follower.id.eq(memberId)),
						"followingCnt"),
					as(select(follow.id.count()).from(follow)
						.where(follow.following.id.eq(memberId)),
						"followerCnt"),
					as(selectOne().from(follow)
							.where(follow.following.id.eq(memberId),
								follow.follower.id.eq(myMemberId)).exists(),
						"myFollowing")
				)
			)
			.from(member)
			.where(member.id.eq(memberId))
			.fetchOne();
	}

	@Override
	public MyDetailResponse findMyDetail(Long memberId) {
		return queryFactory.select(
				fields(MyDetailResponse.class,
					member.id.as("memberId"),
					member.email.as("email"),
					member.nickname.as("nickname"),
					member.gender.as("gender"),
					member.profileImgUrl.as("profileImgUrl"),
					member.createdAt.as("createdAt"),
					as(select(post.id.count()).from(post)
							.where(post.member.id.eq(memberId)),
						"totalPostCnt"),
					as(select(postHeart.id.count()).from(postHeart)
							.where(postHeart.member.id.eq(memberId)),
						"totalPostHeartCnt"),
					as(select(follow.id.count()).from(follow)
							.where(follow.follower.id.eq(memberId)),
						"followingCnt"),
					as(select(follow.id.count()).from(follow)
							.where(follow.following.id.eq(memberId)),
						"followerCnt")
				)
			)
			.from(member)
			.where(member.id.eq(memberId))
			.fetchOne();
	}
}
