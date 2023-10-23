package com.project.snsserver.domain.board.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailResponse {

	private Long postId;

	private String title;

	private String content;

	private String nickname;

	private List<PostImageResponse> postImages;

	private Boolean hasHeart; // 좋아요 여부

	private Long commentCnt;

	private Long heartCnt;

	private LocalDateTime createdAt;

	public void setPostImages(List<PostImageResponse> postImages) {
		this.postImages = postImages;
	}
}
