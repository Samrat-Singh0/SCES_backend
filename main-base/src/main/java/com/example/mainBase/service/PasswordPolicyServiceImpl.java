package com.example.mainBase.service;

import com.example.mainBase.dto.PasswordPolicyRequest;
import com.example.mainBase.dto.PasswordPolicyResponse;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.entities.PasswordPolicy;
import com.example.mainBase.entities.User;
import com.example.mainBase.enums.AuditAction;
import com.example.mainBase.mapper.PasswordPolicyMapper;
import com.example.mainBase.repository.PasswordPolicyRepository;
import com.example.mainBase.util.CommonBeanUtility;
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

  private final AuditLogService auditLogService;

  private final CommonBeanUtility commonBeanUtility;

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
    User user = commonBeanUtility.getLoggedInUser();
    policies.forEach(updatedPolicy -> {
      Optional<PasswordPolicy> optionalPolicy = passwordPolicyRepository.findByCode(
          updatedPolicy.getCode());
      if (optionalPolicy.isPresent()) {
        PasswordPolicy policy = optionalPolicy.get();
        policy.setActive(updatedPolicy.isActive());
        policy.setParameters(updateParameter(policy.getCode(), updatedPolicy.getLength()));
        policy.setLength(updatedPolicy.getLength());
        policy.setRegex(updateRegexPattern(policy.getRegex(), updatedPolicy.getLength()));
        passwordPolicyRepository.save(policy);
        auditLogService.log(user, AuditAction.UPDATED, "Policy", policy.getId());
      }
    });


    return ResponseBuilder.success("Update Successful", null);
  }

  public String updateRegexPattern(String pattern, int length) {
    if(pattern.contains("{")) {
      return pattern.replaceAll("\\{\\d+,}", "{" + length + ",}");
    }
    return pattern;
  }

  public String updateParameter(String code, int length) {
    return switch (code) {
      case "MIN_LENGTH" -> "Minimum " + length + " character(s)";
      case "UPPERCASE" -> "At least " + length + " uppercase letter(s)";
      case "LOWERCASE" -> "At least " + length + " lowercase letter(s)";
      case "DIGIT" -> "At least " + length + " digit(s)";
      case "SPECIAL_CHAR" -> "At least " + length + " special character(s)";
      case "NO_WHITESPACE" -> "No whitespace allowed";
      default -> "";
    };
  }
}
