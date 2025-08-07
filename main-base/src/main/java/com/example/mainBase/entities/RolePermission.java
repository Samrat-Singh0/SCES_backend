package com.example.mainBase.entities;

import com.example.mainBase.compositeKey.RolePermissionId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RolePermission {

  @EmbeddedId
  private RolePermissionId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("roleId")
  @JoinColumn(name = "role_id",nullable = false)
  private Role role;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("permissionId")
  @JoinColumn(name = "permission_id",nullable = false)
  private Permission permission;
}
