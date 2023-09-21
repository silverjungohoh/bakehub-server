package com.project.snsserver.domain.board.model.entity;

import com.project.snsserver.global.entity.BaseCreatedTimeEntity;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostHashtag extends BaseCreatedTimeEntity {

	@Id
	@Column(name = "post_hashtag_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", updatable = false)
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hashtag_id", updatable = false)
	private Hashtag hashtag;
}
