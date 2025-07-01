package com.example.mainBase.controller;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.service.AnalyticsApiClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main/analytics")
@RequiredArgsConstructor
public class AnalyticsApiClientController {

  private final AnalyticsApiClientService analyticsApiClientService;

  @GetMapping("/list")
  public ResponseEntity<ResponseDto> getAnalyticsData() {
    return analyticsApiClientService.getAnalytics();
  }
}
