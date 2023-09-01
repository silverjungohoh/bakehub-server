package com.project.snsserver.domain.board.model.entity;

import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.global.entity.BaseCreatedTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostHeart extends BaseCreatedTimeEntity {

    @Id
    @Column(name = "post_heart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", updatable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;
}
