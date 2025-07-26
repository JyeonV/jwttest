package com.example.jwttest.common.jwt;

import com.example.jwttest.common.code.ErrorStatus;
import com.example.jwttest.common.security.CustomUserDetailsService;
import com.example.jwttest.domain.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final CustomUserDetailsService customUserDetailsService;

    private static final String[] WHITE_LIST = {
            "/login",
            "/signup",
            "/signup/admin"
    };

    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String url = request.getRequestURI();

        if(isWhiteList(url)) {
            chain.doFilter(request, response);
            return;
        }

        String token = jwtUtil.resolveAccessToken(request);

        if(isValidToken(token)) {
            Claims claims = jwtUtil.getClaims(token);
            String username = claims.get("username", String.class);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // spring security 의 표준 인증 객체
            // 1. Principle(주체, 보통 사용자 email 또는 User 객체)
            // 2. credentials(자격정보, 보통 비밀번호이며 인증 후엔 null로 둔다)
            // 3. authorities(권한 목록, 위에서 만든 ROLE_USER 같은 권한들)
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // 생성한 authentication 을 security context holder 에 넣어주며 이후 요청 처리 과정(컨트롤러 등)에서 인증 정보에 접근 가능
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private boolean isValidToken(String token) {
        if(!jwtUtil.validateToken(token)) {
            throw new JwtCustomException(ErrorStatus.INVALID_TOKEN);
        }
        return true;
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
