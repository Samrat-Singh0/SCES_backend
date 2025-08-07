package com.example.mainBase.service;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.entities.User;
import com.example.mainBase.util.AESUtil;
import com.example.mainBase.util.CommonBeanUtility;
import com.example.mainBase.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AnalyticsApiClientServiceImpl implements AnalyticsApiClientService {

  private final RestTemplate restTemplate;
  private final OktaTokenService oktaTokenService;

  @Value("${reporting.api.url}")
  private String reportAndAnalyticsBaseUrl;

  private final SecretKeyService secretKeyService;

  private final CommonBeanUtility commonBeanUtility;

  @Override
  public ResponseEntity<ResponseDto> getAnalytics() throws Exception {

    User user = commonBeanUtility.getLoggedInUser();

    String encryptedUserCode = AESUtil.encrypt(user.getCode(), secretKeyService.getKey());


    String url = reportAndAnalyticsBaseUrl + "/api/analytics/list";
    String token = oktaTokenService.getAccessToken();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    HttpEntity<String> requestEntity = new HttpEntity<>(encryptedUserCode, headers);

    ResponseEntity<ResponseDto> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ResponseDto.class);
    if(response.getBody() != null) {
      return ResponseBuilder.success("Course Report", response.getBody().getBody());
    }

    return ResponseBuilder.getFailedMessage("No data found");

  }
}
