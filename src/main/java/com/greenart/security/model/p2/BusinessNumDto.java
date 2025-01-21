package com.greenart.security.model.p2;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
public class BusinessNumDto {

    private final String busiNum;
    private final long userId;
}
