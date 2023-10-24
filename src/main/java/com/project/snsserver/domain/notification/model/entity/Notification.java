package com.project.snsserver.domain.notification.model.entity;

import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.notification.type.NotificationType;
import com.project.snsserver.global.entity.BaseCreatedTimeEntity;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseCreatedTimeEntity {

	@Id
	@Column(name = "notification_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String content;

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", updatable = false)
	private Member member;

	private String relatedUrl;
}
