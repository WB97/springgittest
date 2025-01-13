package com.greenart.security.controller;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CsvReader {

    public void read() {
        try {
            File file = new File("C:/Users/rmwl2/spring/security/files/police_office_test.csv");
//            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line = "";
            br.readLine(); // csv 1열 데이터를 header로 인식

            while ((line = br.readLine()) != null) {
                String[] stringArray = line.split(";");
                System.out.println("리스트 출력 : " + Arrays.toString(stringArray));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
