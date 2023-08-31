package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.awss3.service.AwsS3Service;
import com.project.snsserver.domain.board.model.dto.EditPostRequest;
import com.project.snsserver.domain.board.model.dto.EditPostResponse;
import com.project.snsserver.domain.board.model.dto.PostImageResponse;
import com.project.snsserver.domain.board.model.entity.Post;
import com.project.snsserver.domain.board.model.entity.PostImage;
import com.project.snsserver.domain.board.repository.jpa.PostImageRepository;
import com.project.snsserver.domain.board.repository.jpa.PostRepository;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.global.error.exception.BoardException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static com.project.snsserver.global.error.type.BoardErrorCode.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final String DIR = "post";

    private final AwsS3Service awsS3Service;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    @Override
    @Transactional
    public EditPostResponse writePost(EditPostRequest request, List<MultipartFile> files, Member member) {

        if(!Objects.isNull(files) && files.size() > 5) {
            throw new BoardException(IMAGE_COUNT_EXCEEDED);
        }

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
                .build();

        postRepository.save(post);

        List<String> imageUrls = Objects.isNull(files)
                ? new ArrayList<>()
                : awsS3Service.uploadFiles(files, DIR);

        List<PostImageResponse> postImgResponse = new ArrayList<>();

        if(!imageUrls.isEmpty()) {
            for(String url : imageUrls) {
                PostImage image = PostImage.builder()
                        .postImageUrl(url)
                        .post(post)
                        .build();

                postImageRepository.save(image);
                postImgResponse.add(PostImageResponse.fromEntity(image));
            }
        }
        return EditPostResponse.fromEntity(post, postImgResponse);
    }

    @Override
    @Transactional
    public EditPostResponse updatePost(Long postId, EditPostRequest request, Member member) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BoardException(POST_NOT_FOUND));

        if(!Objects.equals(post.getMember().getEmail(), member.getEmail())) {
            throw new BoardException(FAIL_TO_UPDATE_POST);
        }

        post.update(request.getTitle(), request.getContent());

        List<PostImageResponse> postImgResponse = post.getPostImages()
                .stream().map(PostImageResponse :: fromEntity)
                .collect(Collectors.toList());

        return EditPostResponse.fromEntity(post, postImgResponse);
    }

    @Override
    @Transactional
    public Map<String, String> deletePost(Long postId, Member member) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BoardException(POST_NOT_FOUND));

        if(!Objects.equals(post.getMember().getEmail(), member.getEmail())) {
            throw new BoardException(FAIL_TO_DELETE_POST);
        }

        List<PostImage> postImages = postImageRepository.findAllByPost(post);
        if(!postImages.isEmpty()) {
            postImages.forEach((postImage -> awsS3Service.deleteFile(postImage.getPostImageUrl(), DIR)));
        }

        postRepository.delete(post);
        return getMessage("게시물이 삭제되었습니다.");
    }

    @Override
    @Transactional
    public PostImageResponse addPostImage(Long postId, MultipartFile file) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BoardException(POST_NOT_FOUND));

        String imageUrl = awsS3Service.uploadFile(file, DIR);

        PostImage postImage = PostImage.builder()
                .post(post)
                .postImageUrl(imageUrl)
                .build();

        postImageRepository.save(postImage);
        return PostImageResponse.fromEntity(postImage);
    }

    private static Map<String, String> getMessage(String message) {
        Map<String, String> result = new HashMap<>();
        result.put("result", message);
        return result;
    }
}