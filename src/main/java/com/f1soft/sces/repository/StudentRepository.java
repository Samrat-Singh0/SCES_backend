package com.f1soft.sces.repository;

import com.f1soft.sces.entities.Student;
import com.f1soft.sces.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

  Student findByUser(User user);

  Optional<Student> findByUser_Id(Long userId);

  Student findByCode(String code);
}
