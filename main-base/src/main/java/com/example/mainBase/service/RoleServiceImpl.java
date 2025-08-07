package com.example.mainBase.service;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.dto.RoleDto;
import com.example.mainBase.dto.RoleResponseDto;
import com.example.mainBase.entities.Permission;
import com.example.mainBase.entities.Role;
import com.example.mainBase.mapper.PermissionMapper;
import com.example.mainBase.mapper.RoleMapper;
import com.example.mainBase.repository.PermissionRepository;
import com.example.mainBase.repository.RoleRepository;
import com.example.mainBase.util.CommonUtility;
import com.example.mainBase.util.ResponseBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;

  @Override
  public ResponseEntity<ResponseDto> getAllRoles() {
    List<Role> roles = roleRepository.findAll();

    for (Role role : roles) {
      List<Permission> permissions = permissionRepository.findByRole(role);
      role.setPermissions(permissions);
    }

    List<Permission> permissions = permissionRepository.findAll();

    RoleResponseDto responseDto = RoleResponseDto.builder()
        .roles(RoleMapper.INSTANCE.toRoleDtoList(roles))
        .permissions(PermissionMapper.INSTANCE.toPermissionDtoList(permissions))
        .build();

    return ResponseBuilder.success("Fetched roles successfully", responseDto);
  }

  @Override
  public ResponseEntity<ResponseDto> getSavedRoles() {
    List<Role> roles = roleRepository.findAll();
    for (Role role : roles) {
      List<Permission> permissions = permissionRepository.findByRole(role);
      role.setPermissions(permissions);
    }

    return ResponseBuilder.success("Fetched roles successfully", roles);
  }

  @Override
  public ResponseEntity<ResponseDto> saveRolesWithPermissions(Map<String, String[]> rolesPayload) {

    List<Role> roles = roleRepository.findAllByCodeIn(rolesPayload.keySet());

    for(Role role : roles) {
      List<Permission> permissions = Arrays.stream(rolesPayload.get(role.getCode()))
          .map(permissionRepository::findByCode)
          .collect(Collectors.toList());

      role.setPermissions(permissions);
    }

    roleRepository.saveAll(roles);
    return ResponseBuilder.success("Roles and permissions updated successfully.", null);
  }

  @Override
  public ResponseEntity<ResponseDto> addNewRole(RoleDto roleDto) {
    Role role = Role.builder()
        .code(CommonUtility.generateCode("ROLE-"))
        .roleName(roleDto.getRoleName())
        .roleType(roleDto.getRoleType())
        .build();
    roleRepository.save(role);
    return ResponseBuilder.success("Role added successfully", null);
  }
}

