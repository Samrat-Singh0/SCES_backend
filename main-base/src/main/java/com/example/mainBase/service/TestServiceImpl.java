package com.example.mainBase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

  private final RestTemplate restTemplate;

  @Override
  public String hello() {
    return restTemplate.getForObject("http://localhost:8081/report/test", String.class);
  }
}
