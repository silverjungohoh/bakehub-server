package com.project.snsserver.domain.board.service;

import java.util.List;

import com.project.snsserver.domain.board.model.entity.Hashtag;

public interface HashtagService {

	List<Hashtag> createHashTags(List<String> tagNames);
}
