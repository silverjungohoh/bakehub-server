package com.project.snsserver.domain.board.model.dto.response;

import com.project.snsserver.domain.board.model.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditCommentResponse {

	private Long commentId;

	private String content;

	private String nickname;

	private LocalDateTime createdAt;

	public static EditCommentResponse fromEntity(Comment comment) {
		return EditCommentResponse.builder()
			.commentId(comment.getId())
			.content(comment.getContent())
			.nickname(comment.getMember().getNickname())
			.createdAt(comment.getCreatedAt())
			.build();
	}
}
