package com.example.mainBase.controller;

import com.example.mainBase.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class TestController {

  private final TestService testService;

  @GetMapping("/test")
  public String test() {
    return testService.hello();
  }
}
