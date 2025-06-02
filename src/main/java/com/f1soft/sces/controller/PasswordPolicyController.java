package com.f1soft.sces.controller;

import com.f1soft.sces.dto.PasswordPolicyDto;
import com.f1soft.sces.dto.PasswordPolicyResponse;
import com.f1soft.sces.entities.PasswordPolicy;
import com.f1soft.sces.service.PasswordPolicyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/super-admin/password-policy")
@RequiredArgsConstructor
public class PasswordPolicyController {

  private final PasswordPolicyService passwordPolicyService;

  @GetMapping("/active")
  public ResponseEntity<List<PasswordPolicyResponse>> getActivePasswordPolicy() {
    List<PasswordPolicyResponse> activePolicies = passwordPolicyService.getActivePolicies();
    return ResponseEntity.ok(activePolicies);
  }


  @PostMapping("/update")
  public ResponseEntity<?> updatePolicies(@RequestBody List<PasswordPolicyDto> policies) {
    List<PasswordPolicy> updatedPolicies = passwordPolicyService.updatePolicies(policies);
    return ResponseEntity.ok(updatedPolicies);
  }
}
