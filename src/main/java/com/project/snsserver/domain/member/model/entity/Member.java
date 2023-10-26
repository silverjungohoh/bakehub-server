package com.project.snsserver.domain.member.model.entity;

import static com.project.snsserver.domain.member.type.MemberStatus.*;

import java.time.LocalDateTime;

import com.project.snsserver.domain.member.type.Gender;
import com.project.snsserver.domain.member.type.MemberRole;
import com.project.snsserver.domain.member.type.MemberStatus;
import com.project.snsserver.global.entity.BaseTimeEntity;

import lombok.*;

import javax.persistence.*;

import org.hibernate.annotations.Where;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted_at IS NULL")
public class Member extends BaseTimeEntity {

	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String email;

	private String password;

	private String nickname;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private String profileImgUrl;

	@Enumerated(EnumType.STRING)
	private MemberRole role;

	@Enumerated(EnumType.STRING)
	private MemberStatus status;

	private LocalDateTime deletedAt;

	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void updateProfileImg(String profileImgUrl) {
		this.profileImgUrl = profileImgUrl;
	}

	public void withdraw() {
		this.profileImgUrl = null;
		this.nickname = "(알 수 없음)";
		this.password = null;
		this.gender = null;
		this.status = WITHDRAWAL;
		this.deletedAt = LocalDateTime.now();
	}
}
