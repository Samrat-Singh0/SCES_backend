package com.example.mainBase.service;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.dto.RoleDto;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface RoleService {

  ResponseEntity<ResponseDto> getAllRoles();

  ResponseEntity<ResponseDto> getSavedRoles();

  ResponseEntity<ResponseDto> saveRolesWithPermissions(Map<String, String[]> rolesPayload);

  ResponseEntity<ResponseDto> addNewRole(RoleDto roleDto);
}
