package com.example.jwttest.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorReasonDto {
    private final String code;
    private final String message;
}

