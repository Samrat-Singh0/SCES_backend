package com.example.mainBase.controller;

import com.example.mainBase.dto.ReportRequestDto;
import com.example.mainBase.service.ReportingApiClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main/report")
@RequiredArgsConstructor
public class ReportingApiClientController {

  private final ReportingApiClientService reportingApiClientService;

  @PostMapping("/get")
  public ResponseEntity<?> getReport(@RequestBody ReportRequestDto requestDto) throws Exception {
    return reportingApiClientService.getReport(requestDto);
  }
}
