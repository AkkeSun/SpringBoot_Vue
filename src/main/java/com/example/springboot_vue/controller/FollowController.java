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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Log4j2
@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowRepository followRepository;

    @PostMapping
    public ResponseEntity following (@RequestBody FollowDto dto){

        // 존재하는 아이디인지 확인
        userRepository.findById(dto.getTargetId()).orElseThrow(RuntimeException::new);
        userRepository.findById(dto.getFollowingId()).orElseThrow(RuntimeException::new);

        Optional<Follow> followingCheck
                = followRepository.findByTargetUserAndFollowing(dto.getTargetId(), dto.getFollowingId());

        if(!followingCheck.isEmpty()){
            throw new RuntimeException("이미 등록된 팔로워입니다");
        } else {

            // save
            Follow follow = Follow.builder().targetUser(dto.getTargetId()).following(dto.getFollowingId()).build();
            Follow newFollow = followRepository.save(follow);

            // return
            URI createdUri = linkTo(FollowController.class).slash(newFollow.getId()).toUri();
            return ResponseEntity.created(createdUri).body(newFollow);
        }
    }


    @DeleteMapping
    public ResponseEntity unFollow (@RequestBody FollowDto dto) throws Exception {

        // 존재하는 아이디인지 확인
        userRepository.findById(dto.getTargetId()).orElseThrow(RuntimeException::new);
        userRepository.findById(dto.getFollowingId()).orElseThrow(RuntimeException::new);

        // delete
        Follow follow = followRepository.findByTargetUserAndFollowing(dto.getTargetId(), dto.getFollowingId()).orElseThrow(Exception::new);
        followRepository.delete(follow);

        // return
        Resource <Follow> resource = new Resource<>(follow);
        resource.add(linkTo(FollowController.class).withSelfRel());
        return ResponseEntity.ok(resource);

    }

    @GetMapping
    public ResponseEntity getFollower(String targetId){
        List<String> followList = followRepository.getFollowList(targetId);

        List<Resource<String>> resource =
                followList.stream()
                        .map(followingId -> new Resource<>(
                                followingId,
                                linkTo(FollowController.class).withSelfRel())
                        )
                        .collect(Collectors.toList());
        return ResponseEntity.ok().body(resource);

    }
}
