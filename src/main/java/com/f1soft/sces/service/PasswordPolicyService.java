package com.f1soft.sces.service;

import com.f1soft.sces.dto.PasswordPolicyRequest;
import com.f1soft.sces.dto.ResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface PasswordPolicyService {

  ResponseEntity<ResponseDto> getAllPolicies();

  ResponseEntity<ResponseDto> getActivePolicies();

  ResponseEntity<ResponseDto> updatePolicies(List<PasswordPolicyRequest> policies);
}
