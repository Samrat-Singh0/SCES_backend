package com.example.mainBase.repository;

import com.example.mainBase.entities.Course;
import com.example.mainBase.entities.Enrollment;
import com.example.mainBase.entities.Grade;
import com.example.mainBase.entities.Student;
import com.example.mainBase.enums.CompletionStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GradeRepository extends JpaRepository<Grade, Long> {

  Grade findByCode(String code);

  @Query(value = "select g from Grade g "
      + "join Enrollment e "
      + "on e.id=g.enrollment.id "
      + "where g.course.code=:courseCode "
      + "and e.completionStatus='RUNNING'")
  List<Grade> fetchByCourseId(String courseCode);

  @Query(value = "select g from Grade g "
      + "join Enrollment e "
      + "on g.enrollment.id=e.id "
      + "where g.student.id=:studentId "
      + "and e.completionStatus=:completionStatus")
  List<Grade> findByStudentId(Long studentId, CompletionStatus completionStatus);

  Optional<Grade> findByStudentAndCourseAndEnrollment(Student student, Course course,
      Enrollment enrollment);
}
