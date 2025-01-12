package com.greenart.security.config.jwt;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.MalformedInputException;

/*
* OncePerRequestFilter 를 상속받아 요청당 한 번만 실행. (매 요청마다 JWT 를 추출하고 검증)
* 1. 클라이언트 요청에서 Authorization 헤더를 읽어 "Bearer " 접두사를 제거한 뒤 JWT 를 추출.
* 2. JWT 를 사용하여 인증 정보를 생성.
* 3. 인증 객체를 Spring Security 의 SecurityContext 에 저장.
* 4. 이후 필터 체인에서 컨텍스트의 인증 정보를 사용하여 요청을 처리.
* */
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /*
        * 클라이언트의 HTTP 요청 헤더에서 Authorization 값을 가져옵니다.
        * ex) Authorization: Bearer <JWT>
        * */
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);

        String token = getAccessToken(authorizationHeader); // Bearer 부분을 제거하고 토큰값만 추출
        if(token != null) {
            try {
                /*
                * 전달된 토큰을 사용하여 인증 정보를 생성합니다.
                * UserDetails 와 권한 반환
                * */
                Authentication auth = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth); // Spring Security의 컨텍스트에 인증 객체를 저장
            } catch (Exception e) {
                // request 객체에 예외를 저장
                request.setAttribute("exception", e);
            }
        }
        filterChain.doFilter(request, response);
    }

    /*
    * Bearer 의 뒷 부분(token) 추출
    * */
    private String getAccessToken(String authorizationHeader) {
        if(authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
