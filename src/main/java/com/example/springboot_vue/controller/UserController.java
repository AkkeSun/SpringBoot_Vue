package com.example.springboot_vue.controller;

import com.example.springboot_vue.entity.Follow;
import com.example.springboot_vue.entity.FollowDto;
import com.example.springboot_vue.entity.User;
import com.example.springboot_vue.repository.FollowRepository;
import com.example.springboot_vue.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Log4j2
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @PostMapping("/idCheck")
    public ResponseEntity idCheck (String userId){

        Optional<User> byId = userRepository.findById(userId);
        Boolean idExistCheck = false;

        if(byId.isEmpty())
            idExistCheck = true;

        Resource<Boolean> resource = new Resource<>(idExistCheck);
        resource.add(linkTo(UserController.class).withSelfRel());
        return ResponseEntity.ok().body(resource);
    }


    @PostMapping
    public ResponseEntity createUser (@RequestBody User user){

        User newUser = userRepository.save(user);
        URI createdUri = linkTo(UserController.class).slash(newUser.getUserId()).toUri();
        return ResponseEntity.created(createdUri).body(newUser);

    }


    @PostMapping("/login")
    public ResponseEntity login (@RequestBody User user){

        User user2 = userRepository.findByUserIdAndPassword(user.getUserId(), user.getPassword()).get();
        Resource<User> resource = null;

        if(user2 != null)
            resource = new Resource<>(user2);

        resource.add(linkTo(UserController.class).withSelfRel());
        return ResponseEntity.ok().body(resource);
    }


    @GetMapping("/search")
    public ResponseEntity searchUser (String searchId, String targetId, PagedResourcesAssembler<User> assembler){

        Pageable pageable = PageRequest.of(0, 5);

        // 검색 결과
        Page<User> userPage = userRepository.findByUserIdStartingWith(searchId, pageable);
        List<User> userList = userPage.getContent();

        // 팔로우 유무 체크
        List<FollowDto> searchList = new ArrayList<>();
        userList.forEach(user -> {

            FollowDto followDto = FollowDto.builder().searchId(user.getUserId()).build();
            Optional<Follow> followingCheck
                    = followRepository.findByTargetUserAndFollowing(targetId, user.getUserId());

            if(followingCheck.isEmpty()) {
                followDto.setFollowCheck(false);
            } else {
                followDto.setFollowCheck(true);
            }
            searchList.add(followDto);
        });

        // return
        List<Resource<FollowDto>> resource =
                searchList.stream()
                          .map(searchData -> new Resource<>(searchData, linkTo(UserController.class).withSelfRel()))
                          .collect(Collectors.toList());
        return ResponseEntity.ok().body(resource);
    }
}
