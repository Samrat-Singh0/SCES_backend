package com.f1soft.sces.repository;

import com.f1soft.sces.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
