package com.project.snsserver.domain.member.service;

import static com.project.snsserver.global.error.type.MemberErrorCode.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.snsserver.domain.member.model.dto.response.FollowResponse;
import com.project.snsserver.domain.member.model.entity.Follow;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.member.repository.jpa.FollowRepository;
import com.project.snsserver.domain.member.repository.jpa.MemberRepository;
import com.project.snsserver.global.error.exception.MemberException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

	private final MemberRepository memberRepository;
	private final FollowRepository followRepository;

	@Override
	@Transactional
	public Map<String, String> follow(String nickname, Member follower) {

		Member following = memberRepository.findByNickname(nickname)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		if (followRepository.existsByFollowerAndFollowing(follower, following)) {
			throw new MemberException(ALREADY_FOLLOWING_MEMBER);
		}

		Follow follow = Follow.builder()
			.following(following)
			.follower(follower)
			.build();

		followRepository.save(follow);
		return getMessage(String.format("%s님을 팔로우하셨습니다.", nickname));
	}

	@Override
	@Transactional
	public void unfollow(String nickname, Member follower) {

		Member following = memberRepository.findByNickname(nickname)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		Long result = followRepository.deleteByFollowerAndFollowing(follower, following);

		if (result == 0) {
			throw new MemberException(FOLLOW_NOT_FOUND);
		}
	}

	@Override
	public Slice<FollowResponse> getMyFollowingList(Member member, Long lastId, Pageable pageable) {
		return followRepository.findAllFollowingByMemberId(member.getId(), lastId, pageable);
	}

	@Override
	public Slice<FollowResponse> getMyFollowerList(Member member, Long lastId, Pageable pageable) {
		return followRepository.findAllFollowerByMemberId(member.getId(), lastId, pageable);
	}

	private static Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		result.put("result", message);
		return result;
	}
}
