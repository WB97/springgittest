package com.greenart.security.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenart.security.config.security.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Service
public class TokenProvider {

    private final ObjectMapper objectMapper; //Jackson 라이브러리
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public TokenProvider(ObjectMapper objectMapper, JwtProperties jwtProperties) {
        this.objectMapper = objectMapper;
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecretKey())); //43자 이상
    }

    /*
    * 입력된 사용자 정보(JwtUser)와 만료 시간(Duration)을 바탕으로 JWT 를 생성합니다.
    * 실제 토큰 생성은 makeToken 메서드에서 처리됩니다.
    * */
    public String generateToken(JwtUser jwtUser, Duration expiredAt) {
        Date now = new Date();
        return makeToken(jwtUser, new Date(now.getTime() + expiredAt.toMillis()));
    }

    /*
    * JWT 를 실제로 생성하는 메서드로, 클레임(Claim), 헤더(Header), 서명(Signature)을 구성합니다.
    * */
    private String makeToken(JwtUser jwtUser, Date expiry) {
        // JWT 암호화
        return Jwts.builder()
                .header().type("JWT") // JWT의 타입을 "JWT"로 지정.
                .and()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(new Date())
                .expiration(expiry)
                .claim("signedUser", makeClaimByUserToString(jwtUser)) // 사용자 정보를 직렬화(JSON 문자열)하여 "signedUser" 클레임에 저장.
                .signWith(secretKey) // JWT의 유효성을 검증하기 위해 비밀키(secretKey)를 사용해 서명
                .compact(); // 최종적으로 compact()를 호출하여 문자열 형태의 JWT 를 반환.
    }

    private String makeClaimByUserToString(JwtUser jwtUser) {

        try {
            /*
            * 객체 자체를 JWT 에 담고 싶어서 객체를 직렬화.
            * 직렬화된 객체를 클레임에 추가하려면 반드시 문자열 형태여야 합니다.
            * ObjectMapper 를 사용하여 JwtUser 객체를 JSON 문자열로 변환.
            * */
            return objectMapper.writeValueAsString(jwtUser);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //Spring Security에서 인증 처리를 해주어야 한다. 그때 Authentication 객체가 필요.
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = getUserDetailsFromToken(token);
        return userDetails == null
                ? null
                : new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public UserDetails getUserDetailsFromToken(String token) {
        JwtUser jwtUser = getJwtUserFromToken(token);
        MyUserDetails userDetails = new MyUserDetails();
        userDetails.setJwtUser(jwtUser);
        return userDetails;
    }

    public JwtUser getJwtUserFromToken(String token) {
        Claims claims = getClaims(token);
        String json = (String)claims.get("signedUser"); // 클레임 객체(Claims)에서 signedUser 라는 키에 저장된 값을 가져옴.
        JwtUser jwtUser;
        try {
            // JSON 직렬화 형식을 역직렬화하여 Java 객체로 변환.
            // ObjectMapper 를 사용해 JSON 문자열(json)을 JwtUser 객체로 변환.
            jwtUser = objectMapper.readValue(json, JwtUser.class);
        } catch (JsonProcessingException e) {
            // JwtUser 클래스로 변환할 수 없는 데이터가 포함된 경우 JsonProcessingException 예외가 발생.
            throw new RuntimeException(e);
        }
        return jwtUser;
    }

    /*
    * 입력받은 JWT 토큰에서 클레임(Claims)을 추출하여 반환
    * 클레임은 JWT 의 Payload 부분으로, 토큰의 유효성 검증 후 사용 가능한 데이터를 포함.
    * */
    private Claims getClaims(String token) {
        /*
        * parser : Parser 객체를 생성. 전달된 JWT 토큰을 해석하고, 토큰 구조(Header, Payload, Signature)를 분리하며 검증 작업을 수행.
        * verifyWith : JWT 토큰의 서명을 검증할 때 사용할 비밀키(secretKey)를 설정. 동일한 비밀키를 사용해야 토큰의 위변조 여부를 확인할 수 있음.
        * build : 설정된 Parser 객체를 완성하고 반환.
        * parseSignedClaims : 입력받은 JWT 토큰(token)을 해석하고, 서명이 검증된 후 클레임 정보가 포함된 결과를 반환. 서명 검증을 통과하지 못하면 예외가 발생
        * getPayload : 서명이 유효한 JWT의 Payload 부분, 즉 클레임 데이터를 반환. 반환값은 Claims 객체로, 키-값 형태로 저장된 클레임 데이터를 제공합니다.
        * */
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
}
