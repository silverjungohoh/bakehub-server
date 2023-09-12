package com.project.snsserver.domain.member.model.dto;

import com.project.snsserver.domain.member.model.entity.Member;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {

    private String profileImgUrl;

    private String nickname;

    public static ProfileResponse fromEntity(Member member) {
        return ProfileResponse.builder()
                .profileImgUrl(member.getProfileImgUrl())
                .nickname(member.getNickname())
                .build();
    }
}
