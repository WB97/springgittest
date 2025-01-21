package com.greenart.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenart.security.controller.service.CsvWriterAndReader;
import com.greenart.security.controller.service.DfsCoordinateConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    private final ObjectMapper objectMapper;
    private final String apiKey;

    public ApiController(ObjectMapper objectMapper,
                         @Value("${api.AuthKey}")String apiKey
    ) {
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    @GetMapping("/convert")
    public ResponseEntity<DfsCoordinateConverter.Coordinate> convertCoordinates(
            @RequestParam String code,
            @RequestParam double v1,
            @RequestParam double v2) {
        DfsCoordinateConverter.Coordinate result = DfsCoordinateConverter.convert(code, v1, v2);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/load")
    public ResponseEntity<Object> loadJson()  {
//        String result = "";
//        String baseUrl = "https://api.odcloud.kr/api/3074462/v1/uddi:161740bd-8ec5-4734-9a3d-f7a2cde34942";
//        String page = "1";
//        String perPage = "10";
//        String path = String.format("%s?page=%s&perPage=%s&serviceKey=%s", baseUrl, page, perPage, apiKey);
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=37.50973575750427&lon=126.9733865566137&appid=0e8faeec4989507bdf9768c26c2acc79");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-type", "application/json");
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
//            result = bf.readLine();
//
//            CrimeData crimeData = objectMapper.readValue(result, CrimeData.class);
//            log.info("result = {}", result);
//            log.info("crimeData = {}", crimeData);

//            for (CrimeDetails datum : crimeData.getData()) {
//                log.info("범죄 대분류 : {}", datum.getCrimeMajorCategory());
//                log.info("범죄 중분류 : {}", datum.getCrimeMediumCategory());
//                for (String key : datum.getLocation().keySet()) {
//                    log.info("{} = {}", key, datum.getLocation().get(key));
//                }
//            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("OK");
    }

    @GetMapping("/csv-read")
    public ResponseEntity<String> readCsv() {
        CsvWriterAndReader csvReader = new CsvWriterAndReader();
        csvReader.insCsv();
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @GetMapping("/csv-write")
    public ResponseEntity<String> writeCsv() {
        CsvWriterAndReader csvReader = new CsvWriterAndReader();
//        csvReader.write();
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}
