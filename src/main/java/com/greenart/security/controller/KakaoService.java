package com.greenart.security.controller;

import com.greenart.security.model.KakaoApproveDto;
import com.greenart.security.model.KakaoReadyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;

import static com.greenart.security.controller.ApiViewController.kakaoTidSession;

@Slf4j
@Service
public class KakaoService {

    private final String payUrl = "https://open-api.kakaopay.com";
    private final String affiliateCode = "TC0ONETIME";
    private final String secretKey = "SECRET_KEY DEVFAB709132B8EAE076EC0DD92CE11D8D723C8D";

    public String kakaoPayReady() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", secretKey);
        headers.add("Content-Type", "application/json");

        HashMap<String, String> params = new HashMap<>();

        String orderNo = "1";
        String userId = "wook";

        params.put("cid", affiliateCode); // 가맹점 코드 - 테스트용
        params.put("partner_order_id", orderNo); // 주문 번호
        params.put("partner_user_id", userId); // 회원 아이디
        params.put("item_name", "테스트 상품1"); // 상품 명
        params.put("quantity", "1"); // 상품 수량
        params.put("total_amount", "20000"); // 상품 가격
        params.put("tax_free_amount", "1000"); // 상품 비과세 금액
        params.put("approval_url", "http://localhost:8080/api/approve"); // 성공시 url
        params.put("cancel_url", "http://localhost:8080/api/kakaoPayCancle"); // 실패시 url
        params.put("fail_url", "http://localhost:8080/api/kakaoPayFail");

        HttpEntity<HashMap<String, String>> body = new HttpEntity<>(params, headers);

        try {
            KakaoReadyDto kakaoReadyDto = restTemplate.postForObject(new URI(payUrl + "/online/v1/payment/ready"), body, KakaoReadyDto.class);
            log.info("kakaoDto = {}", kakaoReadyDto);
            if(kakaoReadyDto != null) {
                kakaoReadyDto.setPartnerOrderId(orderNo);
                kakaoReadyDto.setPartnerUserId(userId);
                kakaoTidSession.put(Integer.parseInt(orderNo), kakaoReadyDto);
                return kakaoReadyDto.getNextRedirectPcUrl();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/pay";
    }

    public String approve(String pgToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", secretKey);
        headers.add("Content-Type", "application/json");

        HashMap<String, String> params = new HashMap<>();

        String orderNo = "1";
        String userId = "wook";
        KakaoReadyDto session = kakaoTidSession.get(Integer.parseInt(orderNo));

        params.put("cid", affiliateCode); // 가맹점 코드 - 테스트용
        params.put("tid", session.getTid()); // 결제 고유 번호, 준비단계 응답에서 가져옴
        params.put("partner_order_id", orderNo); // 주문 번호
        params.put("partner_user_id", userId); // 회원 아이디
        params.put("pg_token", pgToken); // 준비 단계에서 리다이렉트떄 받은 param 값

        HttpEntity<HashMap<String, String>> body = new HttpEntity<>(params, headers);

        try {
            KakaoApproveDto approveDto = restTemplate.postForObject(new URI(payUrl + "/online/v1/payment/approve"), body, KakaoApproveDto.class);
            log.info("approveDto = {}", approveDto);
            if(approveDto != null) {
                return approveDto.getAid();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/pay";
    }
}
