package com.project.snsserver.domain.board.controller;

import com.project.snsserver.domain.board.service.PostHeartService;
import com.project.snsserver.domain.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "좋아요", description = "게시판 API Document - 좋아요")
public class PostHeartController {

    private final PostHeartService postHeartService;

    /**
     * 좋아요 등록
     */
    @Operation(summary = "게시물 좋아요 등록")
    @PostMapping("/{postId}/hearts")
    public ResponseEntity<Void> pushHeart (@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        postHeartService.pushHeart(postId, userDetails.getMember());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
