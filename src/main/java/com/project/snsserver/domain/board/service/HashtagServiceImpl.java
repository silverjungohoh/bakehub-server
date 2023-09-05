package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.board.model.entity.Hashtag;
import com.project.snsserver.domain.board.repository.jpa.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    @Override
    @Transactional
    public Hashtag createHashtag(String tagName) {

        Optional<Hashtag> optional
                = hashtagRepository.findByName(tagName);

        if (optional.isEmpty()) {
            return hashtagRepository.save(Hashtag.builder().name(tagName).build());
        }

        return optional.get();
    }
}

