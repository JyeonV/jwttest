package com.example.jwttest.common.code;

import com.example.jwttest.common.dto.ErrorReasonDto;
import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();

}
