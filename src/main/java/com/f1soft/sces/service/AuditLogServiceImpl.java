package com.f1soft.sces.service;

import com.f1soft.sces.entities.AuditLog;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.repository.AuditLogRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

  private final AuditLogRepository auditLogRepository;

  @Override
  public void log(User user, String action, String entityName, String entityId) {
//    LocalDateTime now = LocalDateTime.now();
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    String formattedDate = now.format(formatter);

    AuditLog auditLog = AuditLog.builder()
        .user(user)
        .action(action)
        .entityName(entityName)
        .entityId(entityId)
        .actionTime(LocalDateTime.now())
        .details(user.getFullName() + " " + action + " " + entityName)
        .build();

    auditLogRepository.save(auditLog);
  }
}
