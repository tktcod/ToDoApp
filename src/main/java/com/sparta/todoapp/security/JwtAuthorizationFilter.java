package com.sparta.todoapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todoapp.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getJwtFromHeader(req);

        if (!StringUtils.hasText(tokenValue)) {
            // 토큰이 전달되지 않은 경우
            sendErrorResponse(res, "토큰이 전달되지 않았습니다.", HttpStatus.BAD_REQUEST);
            return;
        }

        if (!jwtUtil.validateToken(tokenValue)) {
            // 유효하지 않은 토큰인 경우
            sendErrorResponse(res, "토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
            return;
        }

        Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

        try {
            setAuthentication(info.getSubject());
        } catch (Exception e) {
            log.error(e.getMessage());
            sendErrorResponse(res, "인증 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            return;
        }

        filterChain.doFilter(req, res);
    }

    private void sendErrorResponse(HttpServletResponse res, String errorMessage, HttpStatus status) throws IOException {
        res.setStatus(status.value());
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> response = new HashMap<>();
        response.put("message", errorMessage);
        res.getWriter().write(objectMapper.writeValueAsString(response));
    }


    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null);
    }
}
