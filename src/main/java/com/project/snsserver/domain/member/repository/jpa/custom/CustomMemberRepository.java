package com.project.snsserver.domain.member.repository.jpa.custom;

import com.project.snsserver.domain.member.model.dto.MemberDetailResponse;

public interface CustomMemberRepository {

	MemberDetailResponse findMemberDetailByMemberId(Long memberId);
}
