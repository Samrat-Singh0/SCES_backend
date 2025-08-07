package com.example.mainBase.mapper;

import com.example.mainBase.dto.RoleDto;
import com.example.mainBase.entities.Role;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {

  RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

  RoleDto toRoleDto(Role role);

  List<RoleDto> toRoleDtoList(List<Role> roles);
}
