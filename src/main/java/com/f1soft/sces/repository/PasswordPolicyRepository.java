package com.f1soft.sces.repository;

import com.f1soft.sces.entities.PasswordPolicy;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordPolicyRepository extends JpaRepository<PasswordPolicy, Long> {

  List<PasswordPolicy> findByActiveTrue();

  Optional<PasswordPolicy> findByCode(String policyCode);
}
