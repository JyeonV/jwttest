package com.example.jwttest.common.jwt;

import com.example.jwttest.common.code.ErrorStatus;
import com.example.jwttest.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        try{
            chain.doFilter(request, response);
        } catch (JwtCustomException e) {
            ErrorStatus errorStatus = e.getErrorStatus();

            response.setStatus(errorStatus.getHttpStatus().value());
            response.setContentType("application/json;charset=UTF-8");

            Map<String, Object> errorMap = new HashMap<>();
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("code", errorStatus.getCode());
            errorDetails.put("message", errorStatus.getMessage());
            errorMap.put("error", errorDetails);

            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(errorMap));
        }
    }

}
