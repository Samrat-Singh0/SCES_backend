package com.example.mainBase.repository;

import com.example.mainBase.entities.Role;
import java.util.Collection;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

  List<Role> findAll();

  Role findByCode(String code);

  Role findByRoleType(com.example.mainBase.enums.Role roleType);

  List<Role> findAllByCodeIn(Collection<String> codes);
}
