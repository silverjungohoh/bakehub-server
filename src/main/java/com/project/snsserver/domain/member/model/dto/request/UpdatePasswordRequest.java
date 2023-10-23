package com.project.snsserver.domain.member.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {

	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;

	@NotBlank(message = "새로운 비밀번호를 입력해주세요.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,20}$",
		message = "비밀번호는 숫자를 포함한 영문자 최소 8자, 최대 20자까지 가능합니다.")
	private String newPassword;

	@NotBlank(message = "비밀번호 확인을 입력해주세요.")
	private String passwordCheck;
}
