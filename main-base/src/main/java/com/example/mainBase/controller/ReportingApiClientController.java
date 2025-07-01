package com.example.mainBase.controller;

import com.example.mainBase.service.ReportingApiClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main/report")
@RequiredArgsConstructor
public class ReportingApiClientController {

  private final ReportingApiClientService reportingApiClientService;

  @GetMapping("/course/{documentType}")
  public ResponseEntity<byte[]> getCourseReport(@PathVariable String documentType) {
    return reportingApiClientService.getCourseReport(documentType);
  }

  @GetMapping("/grade/{documentType}/{courseCode}")
  public ResponseEntity<byte[]> getGradeReport(@PathVariable String documentType, @PathVariable String courseCode) {
    return reportingApiClientService.getGradeReport(documentType, courseCode);
  }
}
