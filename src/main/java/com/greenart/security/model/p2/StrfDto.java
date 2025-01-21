package com.greenart.security.model.p2;

import com.greenart.security.model.constant.StrfCategory;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class StrfDto {

    private final long strfId;
    private final StrfCategory category;
    private final String title;
    private final double lat;
    private final double lng;
    private final String address;
    private final String locationId;
    private final String post;
    private final String tell;
    private final String startAt;
    private final String endAt;
    private final String open;
    private final String close;
    private final String restDate;
    private final String explain;
    private final String detail;
    private final String createdAt;
    private final String busiNum;
}
