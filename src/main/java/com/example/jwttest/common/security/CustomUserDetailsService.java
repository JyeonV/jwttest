package com.example.jwttest.common.security;

import com.example.jwttest.common.code.ErrorStatus;
import com.example.jwttest.common.jwt.JwtCustomException;
import com.example.jwttest.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.jwttest.domain.user.entity.User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new JwtCustomException(ErrorStatus.INVALID_CREDENTIALS));

        return new CustomUserDetails(user.getId(), user.getUsername(), user.getUserRole());
    }
}
