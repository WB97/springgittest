package com.greenart.security.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class CrimeDetails {

    @JsonProperty("범죄대분류")
    private String CrimeMajorCategory;

    @JsonProperty("범죄중분류")
    private String CrimeMediumCategory;

    // 지역별 데이터는 Map으로 처리
    @JsonProperty
    private Map<String, Integer> location = new HashMap<>();

    @JsonAnySetter
    public void addRegionData(String key, Integer value) {
        location.put(key, value);
    }
}
