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
}
