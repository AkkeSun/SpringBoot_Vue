package com.example.springboot_vue.controller;

import com.example.springboot_vue.aws.AwsS3Service;
import com.example.springboot_vue.entity.GetListDto;
import com.example.springboot_vue.entity.Post;
import com.example.springboot_vue.entity.PostDto;
import com.example.springboot_vue.entity.User;
import com.example.springboot_vue.repository.PostRepository;
import com.example.springboot_vue.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Log4j2
@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AwsS3Service s3Util;


    @GetMapping("/post")
    public ResponseEntity getPost(GetListDto dto, PagedResourcesAssembler<Post> assembler){
        Page<Post> postList = postRepository.getPostList(dto);
        var pagedResources = assembler.toResource(postList, e -> new Resource(e));
        return ResponseEntity.ok(pagedResources);
    }


    @PostMapping("/post")
    public ResponseEntity createNewContent(@RequestBody PostDto dto){

        User user = userRepository.findById(dto.getUserId()).get();
        Post post = Post.builder().comment(dto.getComment())
                .image(dto.getImage()).likes(dto.getLikes())
                .filter(dto.getCheckedFilter()).writer(user).build();

        Post newPost = postRepository.save(post);
        URI createdUri = linkTo(PostController.class).slash(newPost.getId()).toUri();
        return ResponseEntity.created(createdUri).body(newPost);
    }




}
