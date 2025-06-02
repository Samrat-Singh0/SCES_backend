package com.f1soft.sces.mapper;

import com.f1soft.sces.dto.PasswordPolicyResponse;
import com.f1soft.sces.entities.PasswordPolicy;
import java.util.ArrayList;
import java.util.List;

public class PasswordPolicyMapper {

  public List<PasswordPolicyResponse> toDto(List<PasswordPolicy> passwordPolicy) {
    List<PasswordPolicyResponse> passwordPolicyDto = new ArrayList<>();

    for(PasswordPolicy policy : passwordPolicy) {
      PasswordPolicyResponse passwordPolicyResponse = new PasswordPolicyResponse();
      passwordPolicyResponse.setPolicy_code(policy.getPolicyCode());
      passwordPolicyResponse.setParameters(policy.getParameters());
      passwordPolicyResponse.setRegex(policy.getRegex());
      passwordPolicyResponse.setActive(policy.getActive());

      passwordPolicyDto.add(passwordPolicyResponse);
    }

    return passwordPolicyDto;
  }
}
