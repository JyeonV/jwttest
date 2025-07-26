package com.example.jwttest.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class SignUpRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

}
