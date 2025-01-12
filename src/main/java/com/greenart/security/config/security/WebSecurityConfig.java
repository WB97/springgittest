package com.greenart.security.config.security;

import com.greenart.security.config.jwt.JwtAuthenticationEntryPoint;
import com.greenart.security.config.jwt.TokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //메소드 빈등록이 있어야 의미가 있다. 메소드 빈등록이 싱글톤이 됨.
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean //스프링이 메소드 호출을 하고 리턴한 객체의 주소값을 관리한다. (빈등록)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*
        * Spring Security에서 기본적으로 세션을 사용하지 않도록 설정합니다.
        * JWT와 같은 토큰 기반 인증 방식을 사용하며, 이를 통해 무상태(stateless) 인증이 이루어집니다.
        * 세션(쿠키)을 사용하지 않으므로 클라이언트가 요청마다 인증 정보를 제공해야 합니다.
        * */
        return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(h -> h.disable()) //SSR(Server Side Rendering)이 아니다. 화면을 만들지 않을꺼기 때문에 비활성화 시킨다. HTTP 요청 헤더에 기본 인증 정보를 전달하는 "HTTP Basic 인증" 방식을 비활성화합니다.
                .formLogin(form -> form.disable()) //SSR(Server Side Rendering)이 아니다. 폼로그인 기능 자체를 비활성화
                .csrf(csrf -> csrf.disable()) //SSR(Server Side Rendering)이 아니다. 보안관련 SSR 이 아니면 보안이슈가 없기 때문에 기능을 끈다.
                /*
                * 각 엔드포인트에 대한 인증 규칙 설정
                * */
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/api/feed", "/api/feed/**").authenticated() //로그인이 되어 있어야만 사용 가능
                                .requestMatchers(HttpMethod.GET,"/api/user").authenticated()
                                .requestMatchers(HttpMethod.PATCH,"/api/user/pic").authenticated()
                                .anyRequest().permitAll() //나머지 요청은 모두 허용
                )
                // 인증 실패 시 핸들링 설정
                // 인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때 jwtAuthenticationEntryPoint 를 통해 예외를 처리합니다.
                .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                // tokenAuthenticationFilter 를 UsernamePasswordAuthenticationFilter 이전에 추가
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

//    @Bean
//    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((requests) -> requests
//                .requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans").authenticated()
//                .requestMatchers("/contact", "/notices").permitAll()
//                .anyRequest().authenticated()
//        );
//        http.formLogin(withDefaults());
//        http.httpBasic(withDefaults());
//        return http.build();
//    }


    /*
    * clone-board
    * */
//    @Bean
//    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .csrf(CsrfConfigurer::disable)
//                .httpBasic(HttpBasicConfigurer::disable)
//                .sessionManagement(sessionManagement -> sessionManagement
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers("/", "/api/hello", "/api/v1/auth/**", "/api/v1/search/**", "/file/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/v1/board/**", "/api/v1/user/*").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .exceptionHandling(exceptionHandling ->
//                        exceptionHandling.authenticationEntryPoint(new FailedAuthenticationEntryPoint())
//                )
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        return httpSecurity.build();
//    }
//
////    @Bean
//    protected CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedOrigin("*");
//        configuration.addAllowedMethod("*");
//        configuration.addExposedHeader("*");
//        configuration.addAllowedHeader("*");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//
//        return source;
//    }
//}
//
//class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
//            throws IOException, ServletException {
//        response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("{\"code\":\"AF\", \"message\":\"Authorization Failed\"}");
//    }
}
