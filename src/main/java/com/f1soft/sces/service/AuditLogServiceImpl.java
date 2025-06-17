package com.f1soft.sces.service;

import com.f1soft.sces.entities.AuditLog;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.enums.AuditAction;
import com.f1soft.sces.repository.AuditLogRepository;
import com.f1soft.sces.util.CommonUtility;
import java.time.LocalDateTime;
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


}
