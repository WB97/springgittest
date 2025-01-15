package com.greenart.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenart.security.controller.service.CsvWriterAndReader;
import com.greenart.security.controller.service.DfsCoordinateConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

//@RestController
//@RequestMapping("/api/weather")
public class WeatherController {


}
