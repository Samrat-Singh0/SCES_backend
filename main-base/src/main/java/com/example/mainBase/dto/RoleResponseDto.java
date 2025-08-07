package com.example.mainBase.dto;

import com.example.mainBase.entities.RolePermission;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RoleResponseDto {

  private List<RoleDto> roles;
  private List<PermissionDto> permissions;
}
