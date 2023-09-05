package com.project.snsserver.domain.board.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
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

    private Long commentCnt;

    private Long heartCnt;

    private LocalDateTime createdAt;

    public static PostDetailResponse from (PostResponse post, List<PostImageResponse> images) {
        return PostDetailResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .nickname(post.getNickname())
                .createdAt(post.getCreatedAt())
                .commentCnt(post.getCommentCnt())
                .heartCnt(post.getHeartCnt())
                .postImages(images)
                .build();
    }
}
