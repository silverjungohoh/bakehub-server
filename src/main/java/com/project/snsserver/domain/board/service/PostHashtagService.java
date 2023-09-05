package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.board.model.entity.Post;

import java.util.Set;

public interface PostHashtagService {

    /**
     * hashtag - post hashtag 연결
     */
    void createPostHashtag(Post post, Set<String> tagNames);
}
