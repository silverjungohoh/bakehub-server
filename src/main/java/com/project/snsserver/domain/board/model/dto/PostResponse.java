package com.project.snsserver.domain.board.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
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
