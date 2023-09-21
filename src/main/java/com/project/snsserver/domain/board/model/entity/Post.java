package com.project.snsserver.domain.board.model.entity;

import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.global.entity.BaseTimeEntity;

import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

	@Id
	@Column(name = "post_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", updatable = false)
	private Member member;

	@Builder.Default
	@OneToMany(mappedBy = "post")
	private List<PostImage> postImages = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "post")
	private List<Comment> comments = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "post")
	private List<PostHeart> hearts = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "post")
	private List<PostHashtag> hashtags = new ArrayList<>();

	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
