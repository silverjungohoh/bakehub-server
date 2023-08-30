package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.board.model.dto.EditPostRequest;
import com.project.snsserver.domain.board.model.dto.EditPostResponse;
import com.project.snsserver.domain.member.model.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    /**
     * 글 작성
     */
    EditPostResponse writePost(EditPostRequest request, List<MultipartFile> files, Member member);
}
