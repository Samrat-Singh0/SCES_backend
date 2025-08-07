package com.example.mainBase.compositeKey;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class RolePermissionId {
  private Long roleId;
  private Long permissionId;
}
