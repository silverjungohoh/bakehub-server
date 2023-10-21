package com.project.snsserver.domain.board.model.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostHashtagResponse {

	private Long postHashtagId;

	private String tagName;
}
