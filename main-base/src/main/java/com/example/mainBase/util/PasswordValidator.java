package com.example.mainBase.util;

import com.example.mainBase.dto.PasswordPolicyResponse;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.service.PasswordPolicyService;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordValidator {

  private final PasswordPolicyService passwordPolicyService;

  public boolean validatePassword(String password) {
    ResponseEntity<ResponseDto> response = passwordPolicyService.getActivePolicies();
    ResponseDto responseDto = response.getBody();
    assert responseDto != null;
    Object activePolicies = responseDto.getBody();
    List<PasswordPolicyResponse> policies = (List<PasswordPolicyResponse>) activePolicies;

    for (PasswordPolicyResponse policy : policies) {
      String regex = policy.getRegex();
      if(!Pattern.matches(regex, password)) {
        return false;
      }
    }
    return true;
  }

}
