package com.greenart.security.controller.service;

import com.greenart.security.mapper.ApiMapper;
import com.greenart.security.model.constant.StrfCategory;
import com.greenart.security.model.p2.BusinessNumDto;
import com.greenart.security.model.p2.StrfDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
//        br.readLine(); // csv 1열 데이터를 header 로 인식


        while ((line = br.readLine()) != null) {
            String[] list = line.split(",");
            System.out.println(list[0]);
            strfDtoList.add(StrfDto.builder()
                    .strfId(Long.parseLong(list[0]))
                    .category(StrfCategory.valueOf(list[1]))
                    .title(list[2])
                    .lat(Double.parseDouble(list[3]))
                    .lng(Double.parseDouble(list[4]))
                    .address(list[5])
                    .locationId(Long.parseLong(list[6]))
                    .post(list[7])
                    .tell(list[8])
                    .startAt(list[9])
                    .endAt(list[10])
                    .open(list[11])
                    .close(list[12])
                    .restDate(list[13])
                    .explain(list[14])
                    .detail(list[15])
                    .busiNum(list[17])
                    .build());
        }
        System.out.println(strfDtoList.get(0));
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
