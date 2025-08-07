package com.example.mainBase.mapper;

import com.example.mainBase.dto.PermissionDto;
import com.example.mainBase.entities.Permission;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PermissionMapper {

  PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

  PermissionDto toPermissionDto(Permission permission);

  List<PermissionDto> toPermissionDtoList(List<Permission> permissions);

}
