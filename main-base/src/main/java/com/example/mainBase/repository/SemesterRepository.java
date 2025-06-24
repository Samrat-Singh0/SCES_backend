package com.example.mainBase.repository;

import com.example.mainBase.entities.Semester;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {

  Optional<Semester> findByLabel(String label);
}
