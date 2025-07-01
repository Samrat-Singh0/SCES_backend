//package com.example.reportingAndAnalytics.anaytics.controller;
//
//import com.example.mainBase.dto.ResponseDto;
//import com.example.reportingAndAnalytics.anaytics.service.AnalyticsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/analytics")
//@RequiredArgsConstructor
//public class AnalyticsController {
//
//  private final AnalyticsService analyticsService;
//
//  @GetMapping("/list")
//  public ResponseEntity<ResponseDto> getAnalytics(){
//    return analyticsService.getAnalyticsData();
//  }
//}
