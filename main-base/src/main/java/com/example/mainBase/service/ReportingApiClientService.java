package com.example.mainBase.service;

import org.springframework.http.ResponseEntity;

public interface ReportingApiClientService {

  ResponseEntity<byte[]> getCourseReport(String documentType);

  ResponseEntity<byte[]> getGradeReport(String documentType, String courseCode);

}
