package com.project.snsserver.domain.board.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Long postId;

    private String title;

    private String content;

    private String nickname;

    private Timestamp createdAt;

    private Long commentCnt;

    private Long heartCnt;
}
