package com.example.mainBase.controller;

import com.example.mainBase.dto.PasswordPolicyRequest;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.service.PasswordPolicyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/super/password")
@RequiredArgsConstructor
public class PasswordPolicyController {

  private final PasswordPolicyService passwordPolicyService;

  @GetMapping("/list")
  public ResponseEntity<ResponseDto> getAllPasswordPolicy() {
    return passwordPolicyService.getAllPolicies();
  }

  @GetMapping("/active")
  public ResponseEntity<ResponseDto> getActivePasswordPolicy() {
    return passwordPolicyService.getActivePolicies();
  }

  @PutMapping("/update")
  public ResponseEntity<ResponseDto> updatePolicies(
      @RequestBody List<PasswordPolicyRequest> policies) {
    return passwordPolicyService.updatePolicies(policies);
  }
}
