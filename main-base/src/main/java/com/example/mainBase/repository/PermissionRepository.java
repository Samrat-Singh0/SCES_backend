package com.example.mainBase.repository;

import com.example.mainBase.entities.Permission;
import com.example.mainBase.entities.Role;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PermissionRepository extends CrudRepository<Permission, Long> {

  List<Permission> findAll();

  Permission findByCode(String code);

  @Query(value = "SELECT p FROM Permission p "
      + "INNER JOIN RolePermission rp ON p.id = rp.permission.id "
      + "INNER JOIN Role r ON r.id=rp.role.id "
      + "WHERE r=:role")

  List<Permission> findByRole(Role role);
}
