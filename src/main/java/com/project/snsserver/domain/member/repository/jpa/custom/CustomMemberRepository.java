package com.project.snsserver.domain.member.repository.jpa.custom;

import com.project.snsserver.domain.member.model.dto.response.MemberDetailResponse;
import com.project.snsserver.domain.member.model.dto.response.MyDetailResponse;

public interface CustomMemberRepository {

	MemberDetailResponse findMemberDetailByMemberId(Long myMemberId, Long memberId);

	MyDetailResponse findMyDetail(Long memberId);
}
