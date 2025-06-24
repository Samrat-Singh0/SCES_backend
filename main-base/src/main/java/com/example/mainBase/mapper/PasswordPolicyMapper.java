package com.example.mainBase.mapper;

import com.example.mainBase.dto.PasswordPolicyResponse;
import com.example.mainBase.entities.PasswordPolicy;
import java.util.ArrayList;
import java.util.List;

public class PasswordPolicyMapper {

  public List<PasswordPolicyResponse> toDto(List<PasswordPolicy> passwordPolicy) {
    List<PasswordPolicyResponse> passwordPolicyDto = new ArrayList<>();

    for (PasswordPolicy policy : passwordPolicy) {
      PasswordPolicyResponse passwordPolicyResponse = new PasswordPolicyResponse();
      passwordPolicyResponse.setCode(policy.getCode());
      passwordPolicyResponse.setParameters(policy.getParameters());
      passwordPolicyResponse.setRegex(policy.getRegex());
      passwordPolicyResponse.setActive(policy.getActive());

      passwordPolicyDto.add(passwordPolicyResponse);
    }

    return passwordPolicyDto;
  }
}
