package com.example.springboot_vue.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowDto {

    // 팔로우 등록 & 삭제를 위한 변수
    private String targetId;
    private String followingId;

    // 검색결과를 출력하기 위한 변수
    private String searchId;
    private boolean followCheck;

}
