package com.project.snsserver.domain.board.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.snsserver.domain.board.model.entity.Hashtag;
import com.project.snsserver.domain.board.repository.jpa.HashtagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

	private final HashtagRepository hashtagRepository;

	@Override
	@Transactional
	public List<Hashtag> createHashTags(List<String> tagNames) {

		if(Objects.isNull(tagNames) || tagNames.isEmpty()) {
			return null;
		}

		// TODO : 기존에 저장된 tag name 목록 가져오기
		List<Hashtag> hashtags = hashtagRepository.findAllByNameIn(tagNames);
		List<String> hashtagNames = hashtags.stream()
			.map(Hashtag :: getName)
			.collect(Collectors.toList());

		// TODO : 저장되어 있지 않은 tag name 저장하기
		for(String name : tagNames) {
			if(!hashtagNames.contains(name)) {
				Hashtag newHashtag = saveNewHashtag(name);
				hashtags.add(newHashtag);
			}
		}
		return hashtags;
	}

	private Hashtag saveNewHashtag(String tagName) {
		return hashtagRepository.save(new Hashtag(tagName));
	}
}

