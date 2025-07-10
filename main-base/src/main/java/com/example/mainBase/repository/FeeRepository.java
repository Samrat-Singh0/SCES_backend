package com.example.mainBase.repository;

import com.example.mainBase.entities.Fee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeRepository extends JpaRepository<Fee, Long> {

  List<Fee> findByEnrollmentId(Long enrollmentId);
}
