package com.project.snsserver.domain.board.model.dto;

import com.project.snsserver.domain.board.model.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditCommentResponse {

    private Long commentId;

    private String content;

    private String nickname;

    private Timestamp createdAt;

    public static EditCommentResponse fromEntity(Comment comment) {
        return EditCommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getMember().getNickname())
                .createdAt(
                        Objects.isNull(comment.getCreatedAt()) ?
                                Timestamp.from(Instant.MIN)
                                : Timestamp.valueOf(comment.getCreatedAt())
                )
                .build();
    }
}
