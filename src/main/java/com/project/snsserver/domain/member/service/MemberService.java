package com.project.snsserver.domain.member.service;

import com.project.snsserver.domain.member.model.dto.request.LoginRequest;
import com.project.snsserver.domain.member.model.dto.request.LogoutRequest;
import com.project.snsserver.domain.member.model.dto.request.SignUpRequest;
import com.project.snsserver.domain.member.model.dto.request.UpdateNicknameRequest;
import com.project.snsserver.domain.member.model.dto.request.UpdatePasswordRequest;
import com.project.snsserver.domain.member.model.dto.request.VerifyAuthCodeRequest;
import com.project.snsserver.domain.member.model.dto.request.WithdrawRequest;
import com.project.snsserver.domain.member.model.dto.response.LoginResponse;
import com.project.snsserver.domain.member.model.dto.response.MemberDetailResponse;
import com.project.snsserver.domain.member.model.dto.response.ProfileResponse;
import com.project.snsserver.domain.member.model.dto.response.ReissueTokenResponse;
import com.project.snsserver.domain.member.model.dto.response.SignUpResponse;
import com.project.snsserver.domain.member.model.entity.Member;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface MemberService {

	/**
	 * 이메일 중복 확인
	 */
	Map<String, String> checkEmailDuplicate(String email);

	/**
	 * 닉네임 중복 확인
	 */
	Map<String, String> checkNicknameDuplicate(String nickname);

	/**
	 * 이메일 인증번호 전송
	 */
	Map<String, String> sendEmailAuthCode(String email);

	/**
	 * 이메일 인증번호 확인
	 */
	Map<String, String> verifyEmailAuthCode(VerifyAuthCodeRequest request);

	/**
	 * 회원 가입
	 */
	SignUpResponse signUp(MultipartFile file, SignUpRequest request);

	/**
	 * 회원 로그인
	 */
	LoginResponse login(LoginRequest request);

	/**
	 * token 재발급
	 */
	ReissueTokenResponse reissueAccessToken(String refreshToken, Member member);

	/**
	 * 회원 로그아웃
	 */
	Map<String, String> logout(LogoutRequest request, String email);

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
	 * 회원 정보 조회
	 */
	MemberDetailResponse getMemberDetail(Member member);

	/**
	 * 회원 프로필 조회
	 */
	ProfileResponse getMemberProfile(Member member);
}
