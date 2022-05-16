package com.example.springboot_vue.repository;

import com.example.springboot_vue.entity.GetListDto;
import com.example.springboot_vue.entity.Post;
import org.springframework.data.domain.Page;

public interface PostQueryDsl {

    Page<Post> getPostList(GetListDto dto);
}
