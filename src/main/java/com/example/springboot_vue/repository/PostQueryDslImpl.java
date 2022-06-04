package com.example.springboot_vue.repository;

import com.example.springboot_vue.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;


public class PostQueryDslImpl implements PostQueryDsl{

    @Autowired
    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    private FollowRepository followRepository;

    public PostQueryDslImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    QPost post = new QPost("post");
    QUser user = new QUser("user");

    @Transactional
    public Page <Post> getPostList(GetListDto dto) {

        BooleanBuilder builder = new BooleanBuilder();

        // 내가 작성한 데이터 읽어오기
        builder.or(post.writer.userId.eq(dto.getId()));

        // 내가 팔로우하는 데이터 읽어오기
        List<String> followingList = followRepository.getFollowList(dto.getId());

        if(followingList.size() != 0){
            followingList.forEach( followId -> {
                builder.or(post.writer.userId.eq(followId));
            });
        }

        Pageable pageable = PageRequest.of(dto.getPage(), 5);

        QueryResults<Post> result = jpaQueryFactory.selectFrom(post)
                                    .limit(pageable.getPageSize()) // 출력할 데이터 수
                                    .offset(pageable.getOffset())  // 출력할 페이지 인덱스
                                    .where(builder)
                                    .orderBy(post.id.desc())
                                    .innerJoin(post.writer, user) // fetch Lazy 풀어주기
                                    .fetchJoin()                  // fetch Lazy 풀어주기
                                    .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
}
