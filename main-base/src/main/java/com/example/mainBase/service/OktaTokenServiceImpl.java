package com.example.mainBase.service;

import com.example.mainBase.dto.OktaTokenRequestDto;
import com.example.mainBase.dto.OktaTokenResponseDto;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OktaTokenServiceImpl implements OktaTokenService {

  private final RestTemplate restTemplate;

  @Value("${oauth.api.url}")
  private String tokenUrl;

  @Value("${oauth.client.id}")
  private String clientId;

  @Value("${oauth.client.secret}")
  private String clientSecret;

  @Value("${oauth.audience}")
  private String audience;

  private String cachedToken;       //by default, spring beans haru singleton hunxan, so cachedToken ko naya instance bandaina
  private Instant tokenExpiry;

  public OktaTokenServiceImpl(RestTemplateBuilder restTemplateBuilder) {
    restTemplate = restTemplateBuilder.build();
  }

  @Override
  public synchronized String getAccessToken() {
    //if token already access gareko xaina or token 60 seconds paxi expire hunxa vane -> fetchNewToken()
    if(cachedToken == null || Instant.now().isAfter(tokenExpiry.minusSeconds(60))) {
      fetchNewToken();
    }
    return cachedToken;
  }

  @Override
  public void fetchNewToken() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    OktaTokenRequestDto tokenRequestDto = OktaTokenRequestDto.builder()
        .clientId(clientId)
        .clientSecret(clientSecret)
        .grantType("client_credentials")
        .audience(audience)
        .build();
    HttpEntity<OktaTokenRequestDto> request = new HttpEntity<>(tokenRequestDto, headers);

    try {
      ResponseEntity<OktaTokenResponseDto> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, OktaTokenResponseDto.class);

      if(response.getStatusCode().is2xxSuccessful()) {
        OktaTokenResponseDto responseDto = response.getBody();
        assert responseDto != null;         //check if null, does not handle exception if occurs
        this.cachedToken = responseDto.getAccessToken();
        this.tokenExpiry = Instant.now().plusSeconds(responseDto.getExpiresIn());
      }else {
        throw new RuntimeException("Failed to fetch new token. Status:: " + response.getStatusCode());
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to fetch new token.", e);
    }

  }
}
