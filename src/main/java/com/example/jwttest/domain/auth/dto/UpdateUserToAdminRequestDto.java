package com.example.jwttest.domain.auth.dto;

import com.example.jwttest.domain.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UpdateUserToAdminRequestDto {

    private String username;

    private String nickname;

    private UserRole userRoles;

}
