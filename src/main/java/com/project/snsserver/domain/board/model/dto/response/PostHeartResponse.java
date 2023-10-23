package com.project.snsserver.domain.board.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostHeartResponse {

	private Long postId;

	private Long postHeartCount;

	public static PostHeartResponse from(Long id, Long count) {
		return PostHeartResponse.builder()
			.postId(id)
			.postHeartCount(count)
			.build();
	}
}
