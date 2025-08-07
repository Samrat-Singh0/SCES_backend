package com.example.mainBase.service;

import com.example.mainBase.dto.ReportRequestDto;
import com.example.mainBase.util.AESUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ReportingApiClientServiceImpl implements ReportingApiClientService {

  private final RestTemplate restTemplate;
  private final OktaTokenService oktaTokenService;
  private final SecretKeyService secretKeyService;

  @Value("${reporting.api.url}")
  private String reportAndAnalyticsBaseUrl;

//  @Value("${aes.secret.key}")
//  private String SECRET_KEY;

  @Override
  public ResponseEntity<?> getCourseReport(ReportRequestDto requestDto) throws Exception {

    final String SECRET_KEY = secretKeyService.getKey();

    String encryptedText = AESUtil.encryptObject(requestDto, SECRET_KEY);

    String token = oktaTokenService.getAccessToken();
    String url = reportAndAnalyticsBaseUrl + "/api/report/get/course";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> requestEntity = new HttpEntity<>(encryptedText, headers);

    try {
      return restTemplate.exchange(url, HttpMethod.POST, requestEntity, byte[].class);
    }catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(e.getMessage());
    }
  }

  @Override
  public ResponseEntity<?> getSemesterReport(ReportRequestDto reportRequestDto) throws Exception {
    final String SECRET_KEY = secretKeyService.getKey();

    String encryptedText = AESUtil.encryptObject(reportRequestDto, SECRET_KEY);

    String token = oktaTokenService.getAccessToken();
    String url = reportAndAnalyticsBaseUrl + "/api/report/get/semester";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> requestEntity = new HttpEntity<>(encryptedText, headers);

    try {
      return restTemplate.exchange(url, HttpMethod.POST, requestEntity, byte[].class);
    }catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(e.getMessage());
    }
  }
}
