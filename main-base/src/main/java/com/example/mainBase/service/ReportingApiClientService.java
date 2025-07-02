package com.example.mainBase.service;

import org.springframework.http.ResponseEntity;

public interface ReportingApiClientService {

  ResponseEntity<?> getCourseReport(String documentType);

  ResponseEntity<?> getGradeReport(String documentType, String courseCode);

}
