//package com.example.reportingAndAnalytics.reporting.controller;
//
//import com.example.reportingAndAnalytics.reporting.service.ReportingService;
//import java.io.IOException;
//import lombok.RequiredArgsConstructor;
//import net.sf.jasperreports.engine.JRException;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/report/get")
//@RequiredArgsConstructor
//public class ReportingController {
//
//  private final ReportingService reportingService;
//
//  @GetMapping("/course/{documentType}")
//  public ResponseEntity<byte[]> getCourseReport(@PathVariable String documentType)
//      throws IOException, JRException, ClassNotFoundException {
//    return reportingService.getCourseReport(documentType);
//  }
//
//  @GetMapping("/grade/{documentType}/{courseCode}")
//  public ResponseEntity<byte[]> getGradeReport(@PathVariable String documentType, @PathVariable String courseCode)
//      throws JRException, ClassNotFoundException {
//    return reportingService.getGradeReport(documentType, courseCode);
//  }
//}
