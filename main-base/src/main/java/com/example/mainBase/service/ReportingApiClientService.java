package com.example.mainBase.service;

import com.example.mainBase.dto.ReportRequestDto;
import org.springframework.http.ResponseEntity;

public interface ReportingApiClientService {

  ResponseEntity<?> getCourseReport(ReportRequestDto reportRequestDto) throws Exception;

  ResponseEntity<?> getSemesterReport(ReportRequestDto reportRequestDto) throws Exception;

}
