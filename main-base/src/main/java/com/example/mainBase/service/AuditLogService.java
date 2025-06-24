package com.example.mainBase.service;

import com.example.mainBase.entities.User;
import com.example.mainBase.enums.AuditAction;

public interface AuditLogService {

  void log(User user, AuditAction action, String entityName, Long entityId);

  
}
