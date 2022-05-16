package com.example.springboot_vue.repository;

import com.example.springboot_vue.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByTargetUserAndFollowing(String targetId, String searchId);

    @Query("SELECT f.following FROM Follow f where f.targetUser = :targetId")
    List <String> getFollowList(String targetId);
}
