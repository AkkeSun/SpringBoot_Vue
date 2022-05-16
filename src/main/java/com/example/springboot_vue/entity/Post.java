package com.example.springboot_vue.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"writer", "likeUser"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class Post {

    @Id @GeneratedValue
    private Long id;

    private String image;
    private String comment;
    private int likes;
    private String filter;

    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    @OneToMany(fetch = FetchType.LAZY)
    private List<User> likeUser;
}
