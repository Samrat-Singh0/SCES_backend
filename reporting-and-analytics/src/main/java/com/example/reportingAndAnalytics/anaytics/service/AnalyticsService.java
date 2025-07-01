package com.example.reportingAndAnalytics.anaytics.service;

import com.example.mainBase.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface AnalyticsService {
  ResponseEntity<ResponseDto> getAnalyticsData();
}
