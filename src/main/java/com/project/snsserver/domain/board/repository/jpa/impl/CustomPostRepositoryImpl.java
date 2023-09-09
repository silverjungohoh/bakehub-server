package com.project.snsserver.domain.board.repository.jpa.impl;

import com.project.snsserver.domain.board.model.dto.PostResponse;
import com.project.snsserver.domain.board.repository.jpa.CustomPostRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.project.snsserver.domain.board.model.entity.QComment.comment;
import static com.project.snsserver.domain.board.model.entity.QPost.post;
import static com.project.snsserver.domain.board.model.entity.QPostHashtag.postHashtag;
import static com.project.snsserver.domain.board.model.entity.QPostHeart.postHeart;
import static com.project.snsserver.domain.member.model.entity.QMember.member;
import static com.querydsl.core.types.ExpressionUtils.as;
import static com.querydsl.core.types.Projections.bean;
import static com.querydsl.jpa.JPAExpressions.select;

@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<PostResponse> findAllPostsWithCommentCntAndHeartCnt(Long lastPostId, Pageable pageable) {

        List<PostResponse> posts = queryFactory
                .select(
                        bean(PostResponse.class,
                                post.id.as("postId"),
                                post.title.as("title"),
                                post.content.as("content"),
                                member.nickname.as("nickname"),
                                post.createdAt.as("createdAt"),
                                comment.id.count().as("commentCnt"),
                                as(
                                        select(postHeart.id.count())
                                                .from(postHeart)
                                                .where(postHeart.post.id.eq(post.id)),
                                        "heartCnt"
                                )
                        )
                )
                .from(post)
                .leftJoin(post.comments, comment)
                .leftJoin(post.member, member)
                .where(lastPostId(lastPostId))
                .groupBy(post)
                .limit(pageable.getPageSize() + 1)
                .orderBy(post.createdAt.desc())
                .fetch();
        return checkLastPage(pageable, posts);
    }

    @Override
    public PostResponse findPostByPostId(Long postId) {

        return queryFactory
                .select(
                        bean(PostResponse.class,
                                post.id.as("postId"),
                                post.title.as("title"),
                                post.content.as("content"),
                                member.nickname.as("nickname"),
                                post.createdAt.as("createdAt"),
                                comment.id.count().as("commentCnt"),
                                as(
                                        select(postHeart.id.count())
                                                .from(postHeart)
                                                .where(postHeart.post.id.eq(post.id)),
                                        "heartCnt"
                                )
                        )
                )
                .from(post)
                .leftJoin(post.member, member)
                .leftJoin(post.comments, comment)
                .where(post.id.eq(postId))
                .fetchOne();
    }

    @Override
    public Slice<PostResponse> findAllPostsByHashtag(Long lastPostId, String name, Pageable pageable) {

        List<PostResponse> postsByHashtag = queryFactory.select(
                        bean(PostResponse.class,
                                post.id.as("postId"),
                                post.title.as("title"),
                                post.content.as("content"),
                                member.nickname.as("nickname"),
                                post.createdAt.as("createdAt"),
                                as(select(comment.id.count()).from(comment)
                                                .where(comment.post.id.eq(post.id)),
                                        "commentCnt"),
                                as(select(postHeart.id.count()).from(postHeart)
                                                .where(postHeart.post.id.eq(post.id)),
                                        "heartCnt")
                        )
                )
                .from(postHashtag)
                .leftJoin(postHashtag.post, post)
                .leftJoin(postHashtag.post.member, member)
                .where(lastPostId(lastPostId), postHashtag.hashtag.name.eq(name))
                .limit(pageable.getPageSize() + 1)
                .orderBy(post.createdAt.desc())
                .fetch();

        return checkLastPage(pageable, postsByHashtag);
    }

    @Override
    public Long deleteAllPostByMemberId(Long memberId) {
        return queryFactory.delete(post)
                .where(post.member.id.eq(memberId))
                .execute();
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
