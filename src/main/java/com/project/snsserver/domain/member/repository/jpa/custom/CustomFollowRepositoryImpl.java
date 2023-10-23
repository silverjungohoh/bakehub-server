package com.project.snsserver.domain.member.repository.jpa.custom;

import static com.project.snsserver.domain.member.model.entity.QFollow.*;
import static com.project.snsserver.domain.member.model.entity.QMember.*;
import static com.querydsl.core.types.Projections.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.project.snsserver.domain.member.model.dto.response.FollowResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomFollowRepositoryImpl implements CustomFollowRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<FollowResponse> findAllFollowingByMemberId(Long memberId, Long lastId, Pageable pageable) {
		List<FollowResponse> followings
			= queryFactory.select(
				fields(FollowResponse.class,
					follow.id.as("followId"),
					member.profileImgUrl.as("profileImgUrl"),
					member.nickname.as("nickname"),
					member.email.as("email")
				))
			.from(follow)
			.leftJoin(follow.following, member)
			.where(lastFollowId(lastId), follow.follower.id.eq(memberId))
			.limit(pageable.getPageSize() + 1)
			.orderBy(follow.createdAt.desc())
			.fetch();

		return checkLastPage(pageable, followings);
	}

	@Override
	public Slice<FollowResponse> findAllFollowerByMemberId(Long memberId, Long lastId, Pageable pageable) {
		List<FollowResponse> followers
			= queryFactory.select(
				fields(FollowResponse.class,
					follow.id.as("followId"),
					member.profileImgUrl.as("profileImgUrl"),
					member.nickname.as("nickname"),
					member.email.as("email")
				))
			.from(follow)
			.leftJoin(follow.follower, member)
			.where(lastFollowId(lastId), follow.following.id.eq(memberId))
			.limit(pageable.getPageSize() + 1)
			.orderBy(follow.createdAt.desc())
			.fetch();

		return checkLastPage(pageable, followers);
	}

	private BooleanExpression lastFollowId(Long lastFollowId) {
		if (lastFollowId == null) {
			return null;
		}
		return follow.id.lt(lastFollowId);
	}

	private Slice<FollowResponse> checkLastPage(Pageable pageable, List<FollowResponse> followings) {
		boolean hasNext = false;

		if (followings.size() > pageable.getPageSize()) {
			hasNext = true;
			followings.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(followings, pageable, hasNext);
	}
}
