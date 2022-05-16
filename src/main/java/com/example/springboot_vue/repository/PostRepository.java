package com.example.springboot_vue.repository;

import com.example.springboot_vue.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostQueryDsl {
}
