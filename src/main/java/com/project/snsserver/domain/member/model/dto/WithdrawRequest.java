package com.project.snsserver.domain.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawRequest {

	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;
}
