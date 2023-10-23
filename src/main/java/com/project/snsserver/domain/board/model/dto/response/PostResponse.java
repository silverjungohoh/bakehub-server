package com.project.snsserver.domain.board.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

	private Long postId;

	private String title;

	private String content;

	private String nickname;

	private LocalDateTime createdAt;

	private Long commentCnt;

	private Long heartCnt;
}
