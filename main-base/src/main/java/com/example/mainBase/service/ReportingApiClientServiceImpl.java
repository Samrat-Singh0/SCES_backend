package com.example.mainBase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ReportingApiClientServiceImpl implements ReportingApiClientService {

  private final RestTemplate restTemplate;
  private final OktaTokenService oktaTokenService;

  @Value("${reporting.api.url}")
  private String reportAndAnalyticsBaseUrl;

  @Override
  public ResponseEntity<?> getCourseReport(String documentType) {
    String token = oktaTokenService.getAccessToken();

    String url = reportAndAnalyticsBaseUrl + "/api/report/get/course/" + documentType;
    return getResponse(url, token);
  }

  @Override
  public ResponseEntity<?> getGradeReport(String documentType, String courseCode) {
    String token = oktaTokenService.getAccessToken();
    String url = reportAndAnalyticsBaseUrl + "/api/report/get/grade/" + documentType + "/" + courseCode;
    return getResponse(url, token);
  }

  private ResponseEntity<?> getResponse(String url, String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    try {
      return restTemplate.exchange(url, HttpMethod.GET, requestEntity, byte[].class);

    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("Error during communication.");
    }
  }
}
