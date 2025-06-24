package com.example.mainBase.service;

import com.example.mainBase.dto.PasswordPolicyRequest;
import com.example.mainBase.dto.ResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface PasswordPolicyService {

  ResponseEntity<ResponseDto> getAllPolicies();

  ResponseEntity<ResponseDto> getActivePolicies();

  ResponseEntity<ResponseDto> updatePolicies(List<PasswordPolicyRequest> policies);
}
