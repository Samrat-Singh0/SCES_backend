package com.f1soft.sces.service;

import com.f1soft.sces.dto.PasswordPolicyDto;
import com.f1soft.sces.dto.PasswordPolicyResponse;
import com.f1soft.sces.entities.PasswordPolicy;
import com.f1soft.sces.mapper.PasswordPolicyMapper;
import com.f1soft.sces.repository.PasswordPolicyRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordPolicyServiceImpl implements PasswordPolicyService {
  private final PasswordPolicyRepository passwordPolicyRepository;

  @Override
  public List<PasswordPolicyResponse> getActivePolicies() {
    PasswordPolicyMapper mapper = new PasswordPolicyMapper();

    return mapper.toDto(passwordPolicyRepository.findByActiveTrue());
  }

  @Override
  public List<PasswordPolicy> updatePolicies(List<PasswordPolicyDto> policies) {
    List<PasswordPolicy> updated = new ArrayList<>();

    for(PasswordPolicyDto policyDto : policies) {
      PasswordPolicy policy = passwordPolicyRepository.findByPolicyCode(policyDto.getPolicy_code())
          .orElseThrow(() -> new RuntimeException("Policy code not found"));

      policy.setActive(policyDto.isActive());

      updated.add(passwordPolicyRepository.save(policy));
    }

    return updated;
  }
}
