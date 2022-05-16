package com.example.springboot_vue.repository;

import com.example.springboot_vue.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;


@Transactional
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserIdAndPassword (String id, String password);

    Page<User> findByUserIdStartingWith(String userId, Pageable pageable);
}
