package com.example.jwttest.domain.auth.dto;

import com.example.jwttest.domain.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpResponseDto {

    private String username;

    private String nickname;

    private UserRole userRole;
}
