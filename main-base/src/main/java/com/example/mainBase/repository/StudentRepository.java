package com.example.mainBase.repository;

import com.example.mainBase.entities.Student;
import com.example.mainBase.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Long> {

  Student findByUser(User user);

  Optional<Student> findByUser_Id(Long userId);

  Student findByCode(String code);

  @Query(value = "select s from Student s "
      + "join Enrollment e on s.id=e.student.id "
      + "join EnrollmentCourse ec on ec.enrollment.id=e.id "
      + "join Course c on c.id=ec.course.id "
      + "and e.completionStatus='RUNNING'")
  List<Student> findByCourse_Id(Long courseId);
}
