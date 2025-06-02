package com.f1soft.sces.service;

import com.f1soft.sces.dto.PasswordPolicyDto;
import com.f1soft.sces.dto.PasswordPolicyResponse;
import com.f1soft.sces.entities.PasswordPolicy;
import java.util.List;

public interface PasswordPolicyService {
  List<PasswordPolicyResponse> getActivePolicies();
  List<PasswordPolicy> updatePolicies(List<PasswordPolicyDto> policies);
}
