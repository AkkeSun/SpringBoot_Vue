package com.example.springboot_vue.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "userId")
@ToString
@Builder
public class User {

    @Id
    @Column(length = 15)
    private String userId;

    private String password;

    private String role;

}
