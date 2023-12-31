package com.project.snsserver.domain.board.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

	private Long commentId;

	private String content;

	private String nickname;

	private Boolean isWriter; // 작성자 여부

	private LocalDateTime createdAt;
}
