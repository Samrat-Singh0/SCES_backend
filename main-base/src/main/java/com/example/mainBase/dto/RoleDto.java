package com.example.mainBase.dto;

import com.example.mainBase.entities.Permission;
import com.example.mainBase.enums.Role;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RoleDto {
  private String code;
  private String roleName;
  private Role roleType;
  private List<PermissionDto> permissions;
}
