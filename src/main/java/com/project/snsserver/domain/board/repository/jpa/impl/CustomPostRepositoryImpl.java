package com.project.snsserver.domain.board.repository.jpa.impl;

import com.project.snsserver.domain.board.model.dto.PostResponse;
import com.project.snsserver.domain.board.model.entity.Post;
import com.project.snsserver.domain.board.repository.jpa.CustomPostRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.snsserver.domain.board.model.entity.QComment.comment;
import static com.project.snsserver.domain.board.model.entity.QPost.post;
import static com.project.snsserver.domain.board.model.entity.QPostHeart.postHeart;
import static com.project.snsserver.domain.member.model.entity.QMember.member;

@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<PostResponse> findAllPostsWithCommentCntAndHeartCnt(Long lastPostId, Pageable pageable) {
        List<Tuple> results = queryFactory
                .select(post, comment.id.count(), postHeart.id.count(), member.nickname)
                .from(post)
                .leftJoin(post.comments, comment)
                .leftJoin(post.hearts, postHeart)
                .leftJoin(post.member, member)
                .where(lastPostId(lastPostId))
                .groupBy(post)
                .limit(pageable.getPageSize() + 1)
                .orderBy(post.createdAt.desc())
                .fetch();

        List<PostResponse> posts = results.stream()
                .map((tup) -> {
                    Post p = tup.get(post);
                    String nickname = tup.get(member.nickname);
                    Long commentCnt = tup.get(comment.id.count());
                    Long heartCnt = tup.get(postHeart.id.count());
                    assert p != null;
                    return PostResponse.builder()
                            .postId(p.getId())
                            .title(p.getTitle())
                            .content(p.getContent())
                            .nickname(nickname)
                            .createdAt(Timestamp.valueOf(p.getCreatedAt()))
                            .commentCnt(commentCnt)
                            .heartCnt(heartCnt)
                            .build();
                })
                .collect(Collectors.toList());
        return checkLastPage(pageable, posts);
    }

    private BooleanExpression lastPostId(Long lastPostId) {
        if (lastPostId == null) {
            return null;
        }
        return post.id.lt(lastPostId);
    }

    private Slice<PostResponse> checkLastPage(Pageable pageable, List<PostResponse> posts) {
        boolean hasNext = false;

        // 조회한 게시물의 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음
        if (posts.size() > pageable.getPageSize()) {
            hasNext = true;
            posts.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(posts, pageable, hasNext);
    }
}
