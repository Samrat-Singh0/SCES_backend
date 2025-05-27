package com.f1soft.sces.service;

import com.f1soft.sces.entities.User;

public interface AuditLogService {
  void log(User user, String action, String entityName, String entityId );
}
