package com.project.snsserver.domain.board.model.dto;

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
public class CommentResponse {

    private Long commentId;

    private String content;

    private String nickname;

    private LocalDateTime createdAt;

    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getMember().getNickname())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
