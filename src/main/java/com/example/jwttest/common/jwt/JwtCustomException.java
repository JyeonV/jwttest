package com.example.jwttest.common.jwt;

import com.example.jwttest.common.code.ErrorStatus;
import lombok.Getter;

@Getter
public class JwtCustomException extends RuntimeException {
    private final ErrorStatus errorStatus;

    public JwtCustomException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }
}
