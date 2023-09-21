package com.project.snsserver.domain.board.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostHashtagResponse {

	private Long postHashtagId;

	private String tagName;
}
