package com.project.snsserver.domain.member.model.entity;

import com.project.snsserver.domain.member.type.Gender;
import com.project.snsserver.domain.member.type.MemberRole;
import com.project.snsserver.domain.member.type.MemberStatus;
import com.project.snsserver.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;
}
