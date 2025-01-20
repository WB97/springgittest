package com.greenart.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoReadyDto {

    private String tid;

    @JsonProperty("next_redirect_pc_url")
    private String nextRedirectPcUrl;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonIgnore
    private String partnerOrderId;

    @JsonIgnore
    private String partnerUserId;
}
