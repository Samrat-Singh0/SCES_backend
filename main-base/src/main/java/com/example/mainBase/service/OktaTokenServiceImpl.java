package com.example.mainBase.service;

import com.example.mainBase.dto.OktaTokenRequestDto;
import com.example.mainBase.dto.OktaTokenResponseDto;
import com.example.mainBase.entities.User;
import com.example.mainBase.util.CommonBeanUtility;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OktaTokenServiceImpl implements OktaTokenService {

  private final RestTemplate restTemplate;
  private final CommonBeanUtility commonBeanUtility;

  @Value("${oauth.api.url}")
  private String tokenUrl;

  @Value("${oauth.admin.client.id}")
  private String adminClientId;

  @Value("${oauth.admin.client.secret}")
  private String adminClientSecret;

  @Value("${oauth.admin.audience}")
  private String adminAudience;

  @Value("${oauth.instructor.client.id}")
  private String instructorClientId;

  @Value("${oauth.instructor.client.secret}")
  private String instructorClientSecret;

  @Value("${oauth.instructor.audience}")
  private String instructorAudience;

  @Value("${oauth.student.client.id}")
  private String studentClientId;

  @Value("${oauth.student.client.secret}")
  private String studentClientSecret;

  @Value("${oauth.student.audience}")
  private String studentAudience;

  @Value("${okta.api.uri}")
  private String oktaApiUri;

  @Value("${okta.client.id}")
  private String oktaClientId;

  @Value("${okta.client.audience}")
  private String oktaClientSecret;

  @Value("${okta.client.audience}")
  private String oktaAudience;

  private String cachedToken;       //by default, spring beans haru singleton hunxan, so cachedToken ko naya instance bandaina
  private Instant tokenExpiry;

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

    User user = commonBeanUtility.getLoggedInUser();
    OktaTokenRequestDto tokenRequestDto = getRequestDto(user.getRole().toString());

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

  public OktaTokenRequestDto getRequestDto(String role) {
    OktaTokenRequestDto tokenRequestDto = null;
    switch (role.toLowerCase()) {
      case "super_admin":{
        tokenRequestDto = OktaTokenRequestDto.builder()
            .clientId(adminClientId)
            .clientSecret(adminClientSecret)
            .grantType("client_credentials")
            .audience(adminAudience)
            .build();
        break;
      }
      case "student":{
        tokenRequestDto = OktaTokenRequestDto.builder()
            .clientId(studentClientId)
            .clientSecret(studentClientSecret)
            .grantType("client_credentials")
            .audience(studentAudience)
            .build();
        break;
      }
      case "instructor":{
        tokenRequestDto = OktaTokenRequestDto.builder()
            .clientId(instructorClientId)
            .clientSecret(instructorClientSecret)
            .grantType("client_credentials")
            .audience(instructorAudience)
            .build();
        break;
      }
    }
    return tokenRequestDto;
  }
}
