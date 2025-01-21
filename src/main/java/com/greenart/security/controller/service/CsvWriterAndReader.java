package com.greenart.security.controller.service;

import com.greenart.security.model.constant.StrfCategory;
import com.greenart.security.model.p2.StrfDto;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CsvWriterAndReader {

    private final String filename = "strf";

    public void read() {
        try {
            File file = new File(String.format("D:/Students/yjw/자료/p2/테이블/%s.csv", filename));
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line = "";
            Map<Integer, List<String>> map = new HashMap<>();
            br.readLine(); // csv 1열 데이터를 header 로 인식

            while ((line = br.readLine()) != null) {
                String[] stringArray = line.split(",");
                List<String> list = List.copyOf(List.of(stringArray));
                System.out.println("리스트 출력 : " + Arrays.toString(stringArray));
                map.put(Integer.parseInt(stringArray[0]), list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insCsv() {
        try {
            File file = new File(String.format("D:/Students/yjw/자료/p2/테이블/%s.csv", filename));
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line = "";
            br.readLine(); // csv 1열 데이터를 header 로 인식

            List<StrfDto> strfDtoList = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                String[] list = line.split(",");
                strfDtoList.add(StrfDto.builder()
                                .strfId(Integer.parseInt(list[0]))
                                .category(StrfCategory.valueOf(list[1]))
                                .title(list[2])
                                .lat(Double.parseDouble(list[3]))
                                .lng(Double.parseDouble(list[4]))
                                .address(list[5])
                                .locationId(list[6])
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
//                System.out.println("리스트 출력 : " + Arrays.toString(stringArray));
            }
            for (StrfDto strfDto : strfDtoList) {
                System.out.println(strfDto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
