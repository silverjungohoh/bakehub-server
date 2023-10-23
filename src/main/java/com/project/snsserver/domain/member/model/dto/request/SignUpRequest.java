package com.project.snsserver.domain.member.model.dto.request;

import com.project.snsserver.domain.member.type.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "이메일 형식이 잘못되었습니다.")
	private String email;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,20}$",
		message = "비밀번호는 숫자를 포함한 영문자 최소 8자, 최대 20자까지 가능합니다.")
	private String password;

	@NotBlank(message = "비밀번호 확인을 입력해주세요.")
	private String passwordCheck;

	@NotBlank(message = "닉네임을 입력해주세요.")
	@Size(min = 2, max = 10, message = "닉네임은 최소 2자, 최대 10자까지 가능합니다")
	private String nickname;

	@NotNull(message = "성별을 선택해주세요.")
	private Gender gender;
}
