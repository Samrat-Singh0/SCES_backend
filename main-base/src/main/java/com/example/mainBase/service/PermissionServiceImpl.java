package com.example.mainBase.service;

import com.example.mainBase.entities.Permission;
import com.example.mainBase.repository.PermissionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

  private final PermissionRepository permissionRepository;

  @Override
  public List<Permission> getAllPermissions() {
    return permissionRepository.findAll();
  }
}
