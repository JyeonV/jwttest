package com.example.jwttest.common.config;

import com.example.jwttest.common.code.ErrorStatus;
import com.example.jwttest.common.dto.ErrorReasonDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import com.example.jwttest.common.dto.ErrorResponse;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        ErrorStatus errorStatus = ErrorStatus.ACCESS_DENIED;

        ErrorReasonDto reason = new ErrorReasonDto(errorStatus.getCode(), errorStatus.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(reason);

        response.setStatus(errorStatus.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
