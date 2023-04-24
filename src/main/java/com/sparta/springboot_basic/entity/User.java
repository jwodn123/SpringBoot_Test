package com.sparta.springboot_basic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Column(unique = true) // false이면 중복을 허용
    @Pattern(regexp = "[a-z0-9]{4,10}")
    private String username;

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9!@#\\$%\\^&\\*()_\\+\\-={\\[\\]}\\\\|;:'\",<.>/?]{8,15}")
    private String password;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private UserRole role;


    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
