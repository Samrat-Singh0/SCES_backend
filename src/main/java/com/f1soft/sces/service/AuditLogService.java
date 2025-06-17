package com.f1soft.sces.service;

import com.f1soft.sces.entities.User;
import com.f1soft.sces.enums.AuditAction;

public interface AuditLogService {

  void log(User user, AuditAction action, String entityName, Long entityId);

  
}
