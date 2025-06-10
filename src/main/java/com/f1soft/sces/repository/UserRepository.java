package com.f1soft.sces.repository;

import com.f1soft.sces.entities.User;
import com.f1soft.sces.enums.Status;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

  Page<User> findAllByStatus(Status status, Pageable pageable);

  List<User> findAllByStatus(Status status);

  Optional<User> findByEmail(String email);

  Optional<User> findByUserCode(String userCode);


}
