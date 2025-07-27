package com.example.jwttest.domain.auth.service;

import com.example.jwttest.common.code.ErrorStatus;
import com.example.jwttest.common.error.ApiException;
import com.example.jwttest.common.jwt.JwtUtil;
import com.example.jwttest.domain.auth.dto.LoginResponseDto;
import com.example.jwttest.domain.auth.dto.SignUpResponseDto;
import com.example.jwttest.domain.auth.dto.UpdateUserToAdminRequestDto;
import com.example.jwttest.domain.user.entity.User;
import com.example.jwttest.domain.user.entity.UserRole;
import com.example.jwttest.domain.user.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public SignUpResponseDto signup(String username, String password, String nickname) {
        User user = new User(username, password, nickname);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ApiException(ErrorStatus.USER_ALREADY_EXISTS);
        }
        return new SignUpResponseDto(user.getUsername(), user.getNickname(), user.getUserRole());
    }

    public LoginResponseDto login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorStatus.INVALID_CREDENTIALS));

        if(!password.equals(user.getPassword())) {
            throw new ApiException(ErrorStatus.INVALID_CREDENTIALS);
        }

        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getUsername(), user.getUserRole());

        return new LoginResponseDto(accessToken);
    }

    @Transactional
    public SignUpResponseDto signupAdmin(@NotBlank String username, @NotBlank String password, @NotBlank String nickname) {
        User user = new User(username, password, nickname);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ApiException(ErrorStatus.USER_ALREADY_EXISTS);
        }
        user.userToAdmin();
        return new SignUpResponseDto(user.getUsername(), user.getNickname(), user.getUserRole());
    }

    @Transactional
    public UpdateUserToAdminRequestDto updateUserToAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus.INVALID_CREDENTIALS));
        user.userToAdmin();
        return new UpdateUserToAdminRequestDto(user.getUsername(), user.getNickname(), user.getUserRole());
    }
}
