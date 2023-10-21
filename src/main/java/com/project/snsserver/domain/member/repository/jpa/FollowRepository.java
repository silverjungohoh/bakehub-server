package com.project.snsserver.domain.member.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.snsserver.domain.member.model.entity.Follow;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.member.repository.jpa.custom.CustomFollowRepository;

public interface FollowRepository extends JpaRepository<Follow, Long>, CustomFollowRepository {

	boolean existsByFollowerAndFollowing(Member follower, Member following);

	Long deleteByFollowerAndFollowing(Member follower, Member following);
}
