package com.project.snsserver.domain.board.model.dto.response;

import com.project.snsserver.domain.board.model.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditPostResponse {

	private Long postId;

	private String nickname;

	public static EditPostResponse fromEntity(Post post) {
		return EditPostResponse.builder()
			.postId(post.getId())
			.nickname(post.getMember().getNickname())
			.build();
	}
}
