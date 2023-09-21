package com.project.snsserver.domain.board.model.dto;

import com.project.snsserver.domain.board.model.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditPostResponse {

	private Long postId;

	private String title;

	private String content;

	private String nickname;

	private List<PostImageResponse> postImages;

	private LocalDateTime createdAt;

	public static EditPostResponse fromEntity(Post post, List<PostImageResponse> postImages) {
		return EditPostResponse.builder()
			.postId(post.getId())
			.title(post.getTitle())
			.content(post.getContent())
			.nickname(post.getMember().getNickname())
			.postImages(postImages)
			.createdAt(post.getCreatedAt())
			.build();
	}
}
