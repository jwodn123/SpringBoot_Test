package com.sparta.springboot_basic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true) // false이면 중복을 허용
    @Pattern(regexp = "[a-z0-9]{4,10}")
    private String username;

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9]{8,15}")
    private String password;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
