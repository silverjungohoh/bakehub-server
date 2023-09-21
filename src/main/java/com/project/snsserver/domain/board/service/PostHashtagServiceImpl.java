package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.board.model.entity.Hashtag;
import com.project.snsserver.domain.board.model.entity.Post;
import com.project.snsserver.domain.board.model.entity.PostHashtag;
import com.project.snsserver.domain.board.repository.jpa.PostHashtagRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostHashtagServiceImpl implements PostHashtagService {

	private final HashtagService hashtagService;
	private final PostHashtagRepository postHashtagRepository;

	@Override
	@Transactional
	public void createPostHashtag(Post post, Set<String> tagNames) {

		if (tagNames.isEmpty())
			return;

		tagNames.stream()
			.map(hashtagService::createHashtag)
			.forEach(hashtag -> mapToPostHashtags(post, hashtag));
	}

	private void mapToPostHashtags(Post post, Hashtag hashtag) {

		PostHashtag postHashtag = PostHashtag.builder()
			.post(post)
			.hashtag(hashtag)
			.build();

		postHashtagRepository.save(postHashtag);
	}
}
