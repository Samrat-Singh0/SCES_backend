package com.example.mainBase.repository;

import com.example.mainBase.entities.Instructor;
import com.example.mainBase.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

  Optional<Instructor> findByCode(String instructorCode);

  Instructor findByUser(User user);
}
