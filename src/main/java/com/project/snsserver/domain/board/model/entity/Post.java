package com.project.snsserver.domain.board.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.global.entity.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
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

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<PostHashtag> postHashtags = new ArrayList<>();

	public Post(String title, String content, Member member) {
		this.title = title;
		this.content = content;
		this.member = member;
	}

	public Post(String title, String content, Member member, List<Hashtag> hashtags) {
		this.title = title;
		this.content = content;
		this.member = member;
		this.postHashtags = mapToPostHashtag(this, hashtags);
	}

	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void update(String title, String content, List<Hashtag> hashtags) {
		this.title = title;
		this.content = content;
		this.postHashtags = mapToPostHashtag(this, hashtags);
	}

	private static List<PostHashtag> mapToPostHashtag(Post post, List<Hashtag> hashtags) {
		return hashtags.stream()
			.map(hashtag -> new PostHashtag(post, hashtag))
			.collect(Collectors.toList());
	}
}
