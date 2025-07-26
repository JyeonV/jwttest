package com.example.jwttest.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.userRole = UserRole.USER;
    }

    public void userToAdmin() {
        this.userRole = UserRole.ADMIN;
    }
}
