package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.awss3.service.AwsS3Service;
import com.project.snsserver.domain.board.model.dto.request.EditPostRequest;
import com.project.snsserver.domain.board.model.dto.response.EditPostResponse;
import com.project.snsserver.domain.board.model.dto.response.PostDetailResponse;
import com.project.snsserver.domain.board.model.dto.response.PostHashtagResponse;
import com.project.snsserver.domain.board.model.dto.response.PostImageResponse;
import com.project.snsserver.domain.board.model.dto.response.PostResponse;
import com.project.snsserver.domain.board.model.entity.Hashtag;
import com.project.snsserver.domain.board.model.entity.Post;
import com.project.snsserver.domain.board.model.entity.PostImage;
import com.project.snsserver.domain.board.repository.jpa.*;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.global.error.exception.BoardException;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.project.snsserver.global.error.type.BoardErrorCode.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private static final String DIR = "post";

	private final AwsS3Service awsS3Service;
	private final PostRepository postRepository;
	private final PostImageRepository postImageRepository;
	private final CommentRepository commentRepository;
	private final PostHeartRepository postHeartRepository;
	private final PostHashtagRepository postHashtagRepository;
	private final HashtagService hashtagService;

	@Override
	@Transactional
	public EditPostResponse writePost(EditPostRequest request, List<MultipartFile> files, Member member) {

		if (!Objects.isNull(files) && files.size() > 5) {
			throw new BoardException(IMAGE_COUNT_EXCEEDED);
		}

		List<Hashtag> hashtags = hashtagService.createHashTags(request.getTagNames());

		Post post = Objects.isNull(hashtags)
			? new Post(request.getTitle(), request.getContent(), member)
			: new Post(request.getTitle(), request.getContent(), member, hashtags);

		Post savedPost = postRepository.save(post);

		List<String> imageUrls = Objects.isNull(files)
			? new ArrayList<>()
			: awsS3Service.uploadFiles(files, DIR);

		if (!imageUrls.isEmpty()) {
			imageUrls.forEach(url -> {
				PostImage image = PostImage.builder()
					.postImageUrl(url)
					.post(savedPost)
					.build();

				postImageRepository.save(image);
			});
		}
		return EditPostResponse.fromEntity(post);
	}

	@Override
	@Transactional
	public EditPostResponse updatePost(Long postId, EditPostRequest request, Member member) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new BoardException(POST_NOT_FOUND));

		if (!Objects.equals(post.getMember().getEmail(), member.getEmail())) {
			throw new BoardException(FAIL_TO_UPDATE_POST);
		}

		// 해당 게시물의 모든 해시태그 삭제 후 저장
		postHashtagRepository.deleteAllPostHashtagByPostId(post.getId());
		List<Hashtag> hashtags = hashtagService.createHashTags(request.getTagNames());

		if (Objects.isNull(hashtags)) {
			post.update(request.getTitle(), request.getContent());
		} else {
			post.update(request.getTitle(), request.getContent(), hashtags);
		}
		return EditPostResponse.fromEntity(post);
	}

	@Override
	@Transactional
	public Map<String, String> deletePost(Long postId, Member member) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new BoardException(POST_NOT_FOUND));

		if (!Objects.equals(post.getMember().getEmail(), member.getEmail())) {
			throw new BoardException(FAIL_TO_DELETE_POST);
		}

		List<PostImage> postImages = postImageRepository.findAllByPost(post);
		if (!postImages.isEmpty()) {
			postImages.forEach((postImage -> awsS3Service.deleteFile(postImage.getPostImageUrl(), DIR)));
		}

		commentRepository.deleteAllCommentByPostId(postId);
		postHeartRepository.deleteAllPostHeartByPostId(postId);
		postImageRepository.deleteAllPostImageByPostId(postId);
		postHashtagRepository.deleteAllPostHashtagByPostId(postId);

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

	@Override
	@Transactional
	public Map<String, String> deletePostImage(Long postId, Long postImageId) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new BoardException(POST_NOT_FOUND));

		PostImage postImage = postImageRepository.findByIdAndPost(postImageId, post)
			.orElseThrow(() -> new BoardException(POST_IMAGE_NOT_FOUND));

		postImageRepository.delete(postImage);
		awsS3Service.deleteFile(postImage.getPostImageUrl(), DIR);

		return getMessage("이미지가 삭제되었습니다.");
	}

	@Override
	@Transactional(readOnly = true)
	public Slice<PostResponse> getPosts(Long lastPostId, String keyword, Pageable pageable) {
		return postRepository.findAllPost(lastPostId, keyword, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public PostDetailResponse getPostDetail(Long postId, Long memberId) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new BoardException(POST_NOT_FOUND));

		PostDetailResponse postDetailResponse
			= postRepository.findPostByPostId(post.getId(), memberId);

		List<PostImageResponse> postImageResponse
			= postImageRepository.findAllPostImageByPostId(post.getId());

		postDetailResponse.setPostImages(postImageResponse);
		return postDetailResponse;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PostHashtagResponse> getPostHashtags(Long postId) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new BoardException(POST_NOT_FOUND));

		return postHashtagRepository.findAllPostHashtagByPostId(post.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public Slice<PostResponse> getPostsByHashtag(Long lastPostId, String tag, Pageable pageable) {
		return postRepository.findAllPostByHashtag(lastPostId, tag, pageable);
	}

	private static Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		result.put("result", message);
		return result;
	}
}
