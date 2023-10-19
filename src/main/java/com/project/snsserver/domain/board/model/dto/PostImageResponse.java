package com.project.snsserver.domain.board.model.dto;

import com.project.snsserver.domain.board.model.entity.PostImage;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostImageResponse {

	private Long postImageId;

	private String postImageUrl;

	private LocalDateTime createdAt;

	public static PostImageResponse fromEntity(PostImage postImage) {
		return PostImageResponse.builder()
			.postImageId(postImage.getId())
			.postImageUrl(postImage.getPostImageUrl())
			.createdAt(postImage.getCreatedAt())
			.build();
	}
}
