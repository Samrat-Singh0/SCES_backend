package com.example.mainBase.controller;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.dto.RoleDto;
import com.example.mainBase.service.RoleService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/super/role")
public class RoleController {

  private final RoleService roleService;

  @GetMapping("/list")
  ResponseEntity<ResponseDto> getAllRoles() {
    return roleService.getAllRoles();
  }

  @PostMapping("/save")
  ResponseEntity<ResponseDto> saveRolesWithPermissions(@RequestBody Map<String, String[]> rolesPayload) {
    return roleService.saveRolesWithPermissions(rolesPayload);
  }

  @PostMapping("/add/role")
  ResponseEntity<ResponseDto> addNewRole(@RequestBody RoleDto roleDto) {
    return roleService.addNewRole(roleDto);
  }

  @GetMapping("/saved/roles")
  ResponseEntity<ResponseDto> getSavedRoles() {
    return roleService.getSavedRoles();
  }
}
