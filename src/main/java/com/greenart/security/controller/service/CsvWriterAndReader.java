package com.greenart.security.controller.service;

import com.greenart.security.mapper.ApiMapper;
import com.greenart.security.model.constant.StrfCategory;
import com.greenart.security.model.p2.BusinessNumDto;
import com.greenart.security.model.p2.StrfDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CsvWriterAndReader {

    private final String filename = "strf";
    private final String filePath;
    private final ApiMapper apiMapper;

    public CsvWriterAndReader(@Value("${file.path}") String filePath, ApiMapper apiMapper) {
        this.filePath = filePath;
        this.apiMapper = apiMapper;
    }

    public void read() {
        try {
            List<StrfDto> strfDtoList = new ArrayList<>();
            List<BusinessNumDto> businessNumDtoList = new ArrayList<>();
            readStrf(strfDtoList);
//            readBusiNum(businessNumDtoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void insCsv() {
        try {
            List<StrfDto> strfDtoList = new ArrayList<>();
            List<BusinessNumDto> businessNumDtoList = new ArrayList<>();
            readStrf(strfDtoList);
//            readBusiNum(businessNumDtoList);
            apiMapper.insStrf(strfDtoList);
//            apiMapper.insBusiNum(businessNumDtoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readStrf(List<StrfDto> strfDtoList) throws IOException {
        File file = new File(filePath + filename + ".csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        String line = "";
        br.readLine(); // csv 1열 데이터를 header 로 인식


        while ((line = br.readLine()) != null) {
            String[] list = line.split(",");
            strfDtoList.add(StrfDto.builder()
                    .category(StrfCategory.valueOf(list[0]))
                    .title(list[1])
                    .lat(Double.parseDouble(list[2]))
                    .lng(Double.parseDouble(list[3]))
                    .address(list[4])
                    .locationId(Long.parseLong(list[5]))
                    .post(list[6])
                    .tell(list[7])
                    .startAt(list[8])
                    .endAt(list[9])
                    .open(list[10])
                    .close(list[11])
                    .restDate(list[12])
                    .explain(list[13])
                    .detail(list[14])
                    .busiNum(list[16])
                    .build());
        }
        System.out.println(strfDtoList);
    }

    private void readBusiNum(List<BusinessNumDto> busiNumList) throws IOException {
        File file = new File(filePath + filename + ".csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        String line = "";
//        br.readLine(); // csv 1열 데이터를 header 로 인식

        while ((line = br.readLine()) != null) {
            String[] list = line.split(",");
            System.out.println(list[0]);
            System.out.println(list[1]);
            busiNumList.add(BusinessNumDto.builder()
                    .busiNum(list[0])
                    .userId(Integer.parseInt(list[1]))
                    .build());
        }
        System.out.println(busiNumList.get(0));
    }
}
