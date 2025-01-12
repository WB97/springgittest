package com.greenart.security.config.jwt;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import java.io.IOException;

/*
* 인증이 실패하거나 유효하지 않은 JWT 가 발견되면 commence 메서드가 호출됨.
* */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final HandlerExceptionResolver resolver;

    //@Qualifier : 동일한 타입의 Bean 이 여러 개 존재할 때 특정 Bean 을 선택적으로 주입. 기본 구현체는 DefaultHandlerExceptionResolver
    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request
                       , HttpServletResponse response
                       , AuthenticationException authException) throws IOException, ServletException {
        // 발생한 인증 예외(AuthenticationException) 또는 다른 이유로 발생한 예외(request.getAttribute("exception"))를 GlobalExceptionHandler (HandlerExceptionResolver) 에 전달.
        Object exception = request.getAttribute("exception") == null ? null : request.getAttribute("exception");
        if(exception != null) {
            resolver.resolveException(request, response, null, (Exception) exception);
        } else {
            resolver.resolveException(request, response, null, (Exception) new MalformedJwtException("인증정보 없음"));
        }
    }
}