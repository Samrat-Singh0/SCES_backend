package com.example.mainBase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PermissionDto {
  private String code;

  private String description;

  private String module;

  private String path;

  private boolean isActive;

  private String iconName;
}
