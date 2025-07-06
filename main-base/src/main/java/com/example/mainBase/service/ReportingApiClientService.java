package com.example.mainBase.service;

import com.example.mainBase.dto.ReportRequestDto;
import org.springframework.http.ResponseEntity;

public interface ReportingApiClientService {

  ResponseEntity<?> getReport(ReportRequestDto reportRequestDto) throws Exception;

}
