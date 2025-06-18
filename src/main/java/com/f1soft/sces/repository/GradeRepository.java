package com.f1soft.sces.repository;

import com.f1soft.sces.entities.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Integer> {

  Grade findByCode(String code);
}
