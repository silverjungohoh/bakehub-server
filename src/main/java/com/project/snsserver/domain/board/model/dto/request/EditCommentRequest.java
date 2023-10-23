package com.project.snsserver.domain.board.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditCommentRequest {

	@NotBlank(message = "내용을 입력해주세요.")
	private String content;
}
