package com.example.team5_project.common.filter;

import com.example.team5_project.common.utils.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j(topic = "JwtFilter")
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse)  servletResponse;
        String requestURI = request.getRequestURI();

        // 로그인 URI로 들어오면 통과
        if (requestURI.equals("/api/members/sign-in") | requestURI.equals("/api/members/sign-up")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 로그인 성공했다고 가정 시
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("JWT 토큰이 필요합니다.");
        }

        String jwt = authorizationHeader.substring(7);

        if (!jwtUtil.validateToken(jwt)) {
            throw new IllegalArgumentException("Token 값이 유효하지 않습니다.");
        }

        log.info("필터 부분을 통과했습니다.");

        String userType = jwtUtil.extractUserType(jwt);
        Long id = Long.parseLong(jwtUtil.extractId(jwt));

        request.setAttribute("user_type", userType);
        request.setAttribute("id", id);

        filterChain.doFilter(request,response);
    }
}
