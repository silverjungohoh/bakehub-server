package com.project.snsserver.domain.member.model.dto;

import com.project.snsserver.domain.member.type.Gender;
import com.project.snsserver.domain.member.type.MemberRole;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDetailResponse {

    private Long memberId;

    private String email;

    private String nickname;

    private Gender gender;

    private MemberRole role;

    private String profileImgUrl;

    private LocalDateTime createdAt; // 가입일

    private Long totalPostCnt; // 작성한 게시물 개수

    private Long totalPostHeartCnt; // 좋아요 누른 게시물 개수
}
