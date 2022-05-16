package com.example.springboot_vue.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@ToString
public class Follow {

    @Id
    @GeneratedValue
    private Long id;

    private String targetUser;

    private String following;
}
