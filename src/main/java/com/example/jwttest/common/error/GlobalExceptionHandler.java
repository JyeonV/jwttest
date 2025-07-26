package com.example.jwttest.common.error;

import com.example.jwttest.common.dto.ErrorReasonDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import com.example.jwttest.common.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handlerCustomException(ApiException e) {
        log.error("CustomException : {}", e.getMessage(), e);

        ErrorReasonDto reason = new ErrorReasonDto(e.getErrorCode().getCode(), e.getErrorCode().getMessage());

        ErrorResponse errorResponse = new ErrorResponse(reason);

        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }

}
