package com.example.mainBase.service;

import com.example.mainBase.dto.PasswordPolicyRequest;
import com.example.mainBase.dto.PasswordPolicyResponse;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.entities.PasswordPolicy;
import com.example.mainBase.mapper.PasswordPolicyMapper;
import com.example.mainBase.repository.PasswordPolicyRepository;
import com.example.mainBase.util.ResponseBuilder;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordPolicyServiceImpl implements PasswordPolicyService {

  private final PasswordPolicyRepository passwordPolicyRepository;

  @Override
  public ResponseEntity<ResponseDto> getAllPolicies() {
    PasswordPolicyMapper mapper = new PasswordPolicyMapper();
    List<PasswordPolicyResponse> passwordPolicyDto = mapper.toDto(
        passwordPolicyRepository.findAll());
    return ResponseBuilder.success("Fetched Policies Successfully", passwordPolicyDto);
  }

  @Override
  public ResponseEntity<ResponseDto> getActivePolicies() {
    List<PasswordPolicy> activePolicies = passwordPolicyRepository.findByActiveTrue();
    PasswordPolicyMapper mapper = new PasswordPolicyMapper();
    List<PasswordPolicyResponse> passwordPolicyDto = mapper.toDto(activePolicies);
    return ResponseBuilder.success("Fetched Policies Successfully", passwordPolicyDto);
  }

  @Override
  public ResponseEntity<ResponseDto> updatePolicies(List<PasswordPolicyRequest> policies) {

    policies.forEach(updatedPolicy -> {
      Optional<PasswordPolicy> policy = passwordPolicyRepository.findByCode(
          updatedPolicy.getCode());
      if (policy.isPresent()) {
        policy.get().setActive(updatedPolicy.isActive());
        passwordPolicyRepository.save(policy.get());
      }
    });

    return ResponseBuilder.success("Update Successful", null);
  }
}
