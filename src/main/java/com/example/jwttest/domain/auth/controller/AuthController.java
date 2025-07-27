package com.example.jwttest.domain.auth.controller;

import com.example.jwttest.domain.auth.dto.*;
import com.example.jwttest.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signup(@Valid @RequestBody SignUpRequestDto requestDto) {
        SignUpResponseDto responseDto = authService.signup(requestDto.getUsername(), requestDto.getPassword(), requestDto.getNickname());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<SignUpResponseDto> signupAdmin(@Valid @RequestBody SignUpRequestDto requestDto) {
        SignUpResponseDto responseDto = authService.signupAdmin(requestDto.getUsername(), requestDto.getPassword(), requestDto.getNickname());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        LoginResponseDto responseDto = authService.login(requestDto.getUsername(), requestDto.getPassword());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/users/{userId}/roles")
    public ResponseEntity<UpdateUserToAdminRequestDto> updateUserToAdmin(@PathVariable Long userId) {
        UpdateUserToAdminRequestDto response = authService.updateUserToAdmin(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
