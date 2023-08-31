package com.project.snsserver.domain.board.model.dto;

import com.project.snsserver.domain.board.model.entity.PostImage;
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
public class PostImageResponse {

    private Long postImageId;

    private String postImageUrl;

    private Timestamp createdAt;

    public static PostImageResponse fromEntity(PostImage postImage) {
        return PostImageResponse.builder()
                .postImageId(postImage.getId())
                .postImageUrl(postImage.getPostImageUrl())
                .createdAt(
                        Objects.isNull(postImage.getCreatedAt()) ?
                                Timestamp.from(Instant.MIN)
                                : Timestamp.valueOf(postImage.getCreatedAt())
                )
                .build();
    }
}
