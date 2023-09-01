package com.project.snsserver.domain.board.controller;

import com.project.snsserver.domain.board.model.dto.PostHeartResponse;
import com.project.snsserver.domain.board.service.PostHeartService;
import com.project.snsserver.domain.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 좋아요 취소
     */
    @Operation(summary = "게시물 좋아요 취소")
    @DeleteMapping("/{postId}/hearts/{postHeartId}")
    public ResponseEntity<Void> cancelHeart(@PathVariable Long postId, @PathVariable Long postHeartId,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        postHeartService.cancelHeart(postId, postHeartId, userDetails.getMember());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 게시물의 좋아요 개수 조회
     */
    @Operation(summary = "게시물 좋아요 개수 조회")
    @GetMapping("/{postId}/hearts")
    public ResponseEntity<PostHeartResponse> getPostHeartCountByPost(@PathVariable Long postId) {

        PostHeartResponse response = postHeartService.getPostHeartCountByPost(postId);
        return ResponseEntity.ok(response);
    }
}
