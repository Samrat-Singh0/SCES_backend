package com.example.mainBase.controller;

import com.example.mainBase.dto.ReportRequestDto;
import com.example.mainBase.service.ReportingApiClientService;
import jakarta.validation.Valid;
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

  @PostMapping("/get/course")
  public ResponseEntity<?> getCourseReport(@RequestBody @Valid ReportRequestDto requestDto) throws Exception {
    return reportingApiClientService.getCourseReport(requestDto);
  }

  @PostMapping("/get/semester")
  public ResponseEntity<?> getSemesterReport(@RequestBody @Valid ReportRequestDto requestDto) throws Exception {
    return reportingApiClientService.getSemesterReport(requestDto);
  }
}
