package com.greenart.security.controller;

import com.greenart.security.model.KakaoReadyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiViewController {

    private final KakaoService kakaoService;
    public static final Map<Integer, KakaoReadyDto> kakaoTidSession = new HashMap<>();

    @GetMapping("/kakao-pay")
    public String getKakaoPay() {
        return "payready";
    }

    @PostMapping("/kakao-pay")
    public String postKakaoPay() {
        return "redirect:" + kakaoService.kakaoPayReady();
    }

    @GetMapping("/pay-wait")
    public String payWait() {
        return "paywait";
    }

    @GetMapping("/approve")
    public String payApprove(@RequestParam("pg_token") String pgToken) {
        return "redirect:" + kakaoService.approve(pgToken);
    }

    @GetMapping("/complete")
    public String payComplete() {
        return "closepay";
    }
}
