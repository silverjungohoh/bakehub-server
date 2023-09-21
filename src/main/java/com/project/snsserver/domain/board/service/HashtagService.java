package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.board.model.entity.Hashtag;

public interface HashtagService {

	/**
	 * 기존에 존재하는 해시태그인지 확인 후 없으면 저장
	 */
	Hashtag createHashtag(String tagName);
}
