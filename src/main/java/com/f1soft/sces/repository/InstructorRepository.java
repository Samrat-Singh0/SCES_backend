package com.f1soft.sces.repository;

import com.f1soft.sces.entities.Instructor;
import com.f1soft.sces.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

  Optional<Instructor> findByCode(String instructorCode);

  Instructor findByUser(User user);
}
