package com.f1soft.sces.repository;

import com.f1soft.sces.entities.Semester;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Integer> {

  Optional<Semester> findByLabel(String label);
}
