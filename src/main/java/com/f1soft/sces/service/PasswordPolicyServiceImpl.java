package com.f1soft.sces.service;

import com.f1soft.sces.dto.PasswordPolicyRequest;
import com.f1soft.sces.dto.PasswordPolicyResponse;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.entities.PasswordPolicy;
import com.f1soft.sces.mapper.PasswordPolicyMapper;
import com.f1soft.sces.repository.PasswordPolicyRepository;
import com.f1soft.sces.util.ResponseBuilder;
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
