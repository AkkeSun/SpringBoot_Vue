package com.example.springboot_vue.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Builder
public class PostDto {

    private String image;
    private String comment;
    private String checkedFilter;
    private int likes;
    private String userId;
}
