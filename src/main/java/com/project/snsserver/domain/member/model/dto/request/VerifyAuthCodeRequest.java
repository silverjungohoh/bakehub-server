package com.project.snsserver.domain.member.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyAuthCodeRequest {

	private String email;

	private String code;
}
