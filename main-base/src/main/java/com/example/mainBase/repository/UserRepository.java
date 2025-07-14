package com.example.mainBase.repository;

import com.example.mainBase.entities.User;
import com.example.mainBase.enums.ActiveStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

  Page<User> findAllByActiveStatus(ActiveStatus activeStatus, Pageable pageable);

  List<User> findAllByActiveStatus(ActiveStatus activeStatus);

  Optional<User> findByEmail(String email);

  User findByCode(String userCode);

  Page<User> findAll(Specification<User> spec, Pageable pageable);

  @Modifying
  @Query("update User u SET u.mustChangePassword=true")
  void setMustChangePasswordForAll();
}
