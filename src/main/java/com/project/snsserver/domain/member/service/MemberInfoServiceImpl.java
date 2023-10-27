package com.project.snsserver.domain.member.service;

import static com.project.snsserver.global.error.type.MemberErrorCode.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.snsserver.domain.awss3.service.AwsS3Service;
import com.project.snsserver.domain.member.model.dto.request.UpdateNicknameRequest;
import com.project.snsserver.domain.member.model.dto.request.UpdatePasswordRequest;
import com.project.snsserver.domain.member.model.dto.request.WithdrawRequest;
import com.project.snsserver.domain.member.model.dto.response.MemberDetailResponse;
import com.project.snsserver.domain.member.model.dto.response.MyDetailResponse;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.member.repository.jpa.MemberRepository;
import com.project.snsserver.global.error.exception.MemberException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberInfoServiceImpl implements MemberInfoService {

	private static final String DIR = "profile";

	private final MemberRepository memberRepository;
	private final AwsS3Service awsS3Service;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public Map<String, String> updatePassword(UpdatePasswordRequest request, String email) {

		Member member = getMemberByEmail(email);

		if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
			throw new MemberException(INCORRECT_NOW_PASSWORD);
		}

		if (!Objects.equals(request.getNewPassword(), request.getPasswordCheck())) {
			throw new MemberException(INCORRECT_PASSWORD_CHECK);
		}

		member.updatePassword(passwordEncoder.encode(request.getNewPassword()));
		return getMessage("비밀번호 변경이 완료되었습니다.");
	}

	@Override
	@Transactional
	public Map<String, String> updateNickname(UpdateNicknameRequest request, String email) {

		Member member = getMemberByEmail(email);

		if (memberRepository.existsByNickname(request.getNickname())) {
			throw new MemberException(DUPLICATED_NICKNAME);
		}

		member.updateNickname(request.getNickname());
		return getMessage("닉네임 변경이 완료되었습니다.");
	}

	@Override
	@Transactional
	public Map<String, String> updateProfileImg(MultipartFile file, String email) {

		Member member = getMemberByEmail(email);

		String newProfileImgUrl = awsS3Service.uploadFile(file, DIR);
		awsS3Service.deleteFile(member.getProfileImgUrl(), DIR);

		member.updateProfileImg(newProfileImgUrl);
		return getMessage("프로필 이미지 변경이 완료되었습니다.");
	}

	@Override
	@Transactional
	public Map<String, String> withdraw(WithdrawRequest request, String email) {

		Member member = getMemberByEmail(email);

		if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
			throw new MemberException(FAIL_TO_WITHDRAWAL);
		}
		awsS3Service.deleteFile(member.getProfileImgUrl(), DIR);
		member.withdraw();

		return getMessage("회원 탈퇴가 완료되었습니다.");
	}

	@Override
	@Transactional(readOnly = true)
	public MemberDetailResponse getMemberDetail(Long myMemberId, Long memberId) {

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		return memberRepository.findMemberDetailByMemberId(myMemberId, member.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public MyDetailResponse getMyDetail(Member member) {
		return memberRepository.findMyDetail(member.getId());
	}

	private Member getMemberByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
	}

	private static Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		result.put("result", message);
		return result;
	}
}
