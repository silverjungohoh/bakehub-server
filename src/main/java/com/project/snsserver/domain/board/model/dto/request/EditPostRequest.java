package com.project.snsserver.domain.board.model.dto.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditPostRequest {

	@NotBlank(message = "제목을 입력해주세요.")
	private String title;

	@NotBlank(message = "내용을 입력해주세요.")
	private String content;

	@Size(max = 5, message = "해시태그는 최대 5개까지 입력 가능합니다.")
	private List<String> tagNames;
}
