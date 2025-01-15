package com.greenart.security.controller.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CsvWriterAndReader {

    public void read() {
        try {
            File file = new File("D:/Students/yjw/springgittest/files/police_office_test.csv");
//            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line = "";
            Map<Integer, List<String>> map = new HashMap<>();
            br.readLine(); // csv 1열 데이터를 header 로 인식

            while ((line = br.readLine()) != null) {
                String[] stringArray = line.split(";");
                List<String> list = List.copyOf(List.of(stringArray));
                System.out.println("리스트 출력 : " + Arrays.toString(stringArray));
                map.put(Integer.parseInt(stringArray[0]), list);
            }
            System.out.println(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void write() {
//        File csv = new File("D:/Students/yjw/springgittest/files/lodging.csv");
//        BufferedWriter bw = null;
//        try {
//            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv), StandardCharsets.UTF_8));
//            bw.write("\uFEFF");
//            for (int i = 0; i < 100; i++) {
//                String testData =  "";
//                bw.write(testData);
//                bw.newLine();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if(bw != null) {
//                    bw.flush();
//                    bw.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
