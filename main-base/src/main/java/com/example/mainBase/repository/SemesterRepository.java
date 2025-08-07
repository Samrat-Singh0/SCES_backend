package com.example.mainBase.repository;

import com.example.mainBase.entities.Semester;
import com.example.mainBase.entities.Student;
import com.example.mainBase.enums.ActiveStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {

  Optional<Semester> findByLabel(String label);

  List<Semester> findByActiveStatus(ActiveStatus activeStatus);

  Semester findTopByOrderByIdAsc();

  Semester findTopByOrderByIdDesc();
}
