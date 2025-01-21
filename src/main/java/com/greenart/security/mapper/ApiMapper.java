package com.greenart.security.mapper;

import com.greenart.security.model.p2.BusinessNumDto;
import com.greenart.security.model.p2.StrfDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApiMapper {

    void insStrf(List<StrfDto> dtoList);

    void insBusiNum(List<BusinessNumDto> dtoList);
}
