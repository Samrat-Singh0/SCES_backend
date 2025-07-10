package com.example.mainBase.service;

import com.example.mainBase.entities.AuditLog;
import com.example.mainBase.entities.User;
import com.example.mainBase.enums.AuditAction;
import com.example.mainBase.exception.ResourceNotFoundException;
import com.example.mainBase.repository.AuditLogRepository;
import com.example.mainBase.util.CommonUtility;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

  private final AuditLogRepository auditLogRepository;

  @Override
  public void log(User user, AuditAction action, String entityName, Long entityId) {

    String fullName = CommonUtility.getFullName(user.getFirstName(), user.getMiddleName(),
        user.getLastName());

    AuditLog auditLog = AuditLog.builder()
        .user(user)
        .action(action)
        .entityName(entityName)
        .entityId(entityId)
        .actionTime(LocalDateTime.now())
        .details(fullName + " " + action + " " + entityName)
        .build();

    auditLogRepository.save(auditLog);
  }

  @Override
  public AuditLog getLog(AuditAction action, String entityName, long entityId) {
    return Optional.ofNullable(
        auditLogRepository.findByEntityIdAndAction(action, entityName, entityId))
        .orElseThrow(() -> new ResourceNotFoundException("Log not found"));
  }
}
