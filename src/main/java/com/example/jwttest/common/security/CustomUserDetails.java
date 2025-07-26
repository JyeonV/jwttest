package com.example.jwttest.common.security;

import com.example.jwttest.domain.user.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Long id;

    private final String username;

    private final UserRole userRole;

    public CustomUserDetails(Long id, String username, UserRole userRole) {
        this.id = id;
        this.username = username;
        this.userRole = userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(() -> "ROLE_" + userRole.name()); }

    @Override
    public String getPassword() { return ""; }

    @Override
    public String getUsername() { return username; }

    @Override // false 일 경우 : 계정이 만료되어 로그인 불가
    public boolean isAccountNonExpired() { return true; }

    @Override // false 일 경우 : 계정이 잠금처리 되어 로그인 불가
    public boolean isAccountNonLocked() { return true; }

    @Override // false 일 경우 : 비밀번호 유효기간이 만료되어 로그인 불가
    public boolean isCredentialsNonExpired() { return true; }

    @Override // false 일 경우 : 계정이 비활성화됨
    public boolean isEnabled() { return true; }

}
