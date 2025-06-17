package com.f1soft.sces.dto;

import com.f1soft.sces.enums.AuditAction;
import lombok.Data;

@Data
public class AuditLogPayload {

  private UserRequestPayload user;
  private AuditAction action;
  private String entityName;
  private Long entityId;
}
