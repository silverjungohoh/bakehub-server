package com.project.snsserver.domain.board.model.entity;

import com.project.snsserver.global.entity.BaseCreatedTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage extends BaseCreatedTimeEntity {

    @Id
    @Column(name = "post_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String postImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", updatable = false)
    private Post post;
}
