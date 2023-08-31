package com.project.snsserver.domain.board.controller;

import com.project.snsserver.domain.board.model.dto.EditPostRequest;
import com.project.snsserver.domain.board.model.dto.EditPostResponse;
import com.project.snsserver.domain.board.model.dto.PostImageResponse;
import com.project.snsserver.domain.board.service.PostService;
import com.project.snsserver.domain.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "게시판", description = "게시판 API Document - 게시물")
public class PostController {

    private final PostService postService;

    /**
     * 글 작성
     */
    @Operation(summary = "글 작성")
    @PostMapping
    public ResponseEntity<EditPostResponse> writePost(@RequestPart(value = "images", required = false) List<MultipartFile> files,
                                                      @RequestPart(value = "data") @Valid EditPostRequest request,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {

        EditPostResponse response = postService.writePost(request, files, userDetails.getMember());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 글 수정
     */
    @Operation(summary = "글 수정")
    @PatchMapping("/{postId}")
    public ResponseEntity<EditPostResponse> updatePost(@PathVariable Long postId,
                                                       @RequestBody @Valid EditPostRequest request,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        EditPostResponse response = postService.updatePost(postId, request, userDetails.getMember());
        return ResponseEntity.ok(response);
    }

    /**
     * 글 삭제
     */
    @Operation(summary = "글 삭제")
    @DeleteMapping("/{postId}")
    public ResponseEntity<Map<String, String>> deletePost(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, String> response = postService.deletePost(postId, userDetails.getMember());
        return ResponseEntity.ok(response);
    }

    /**
     * 이미지 등록
     */
    @Operation(summary = "이미지 등록")
    @PostMapping("/{postId}/images")
    public ResponseEntity<PostImageResponse> addPostImage(@PathVariable Long postId,
                                                          @RequestPart(value = "image") MultipartFile file) {

        PostImageResponse response = postService.addPostImage(postId, file);
        return ResponseEntity.ok(response);
    }

    /**
     * 이미지 삭제
     */
    @Operation(summary = "이미지 삭제")
    @DeleteMapping("/{postId}/images/{postImageId}")
    public ResponseEntity<Map<String, String>> deletePostImage(@PathVariable Long postId,
                                                               @PathVariable Long postImageId) {

        Map<String, String> response = postService.deletePostImage(postId, postImageId);
        return ResponseEntity.ok(response);
    }
}
