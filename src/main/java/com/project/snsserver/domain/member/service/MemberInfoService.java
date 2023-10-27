package com.project.snsserver.domain.member.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.project.snsserver.domain.member.model.dto.request.UpdateNicknameRequest;
import com.project.snsserver.domain.member.model.dto.request.UpdatePasswordRequest;
import com.project.snsserver.domain.member.model.dto.request.WithdrawRequest;
import com.project.snsserver.domain.member.model.dto.response.MemberDetailResponse;
import com.project.snsserver.domain.member.model.entity.Member;

public interface MemberInfoService {

	/**
	 * 회원 비밀번호 수정
	 */
	Map<String, String> updatePassword(UpdatePasswordRequest request, String email);

	/**
	 * 회원 닉네임 수정
	 */
	Map<String, String> updateNickname(UpdateNicknameRequest request, String email);

	/**
	 * 회원 프로필 이미지 수정
	 */
	Map<String, String> updateProfileImg(MultipartFile file, String email);

	/**
	 * 회원 탈퇴
	 */
	Map<String, String> withdraw(WithdrawRequest request, String email);

	/**
	 * 나의 회원 정보 상세 조회
	 */
	MemberDetailResponse getMyMemberDetail(Member member);

	/**
	 * 다른 회원 정보 상세 조회
	 */
	MemberDetailResponse getMemberDetail(Long memberId);
}
