package com.example.reportingAndAnalytics.anaytics.service;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.util.ResponseBuilder;
import com.example.reportingAndAnalytics.anaytics.model.AnalyticDto;
import com.example.reportingAndAnalytics.anaytics.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService{

  private final AnalyticsRepository analyticsRepository;

  @Override
  public ResponseEntity<ResponseDto> getAnalyticsData() {
    AnalyticDto analyticData = analyticsRepository.getAnalyticsList();

    return ResponseBuilder.success("Fetched Analytics Data", analyticData);
  }
}
