package com.f1soft.sces.repository;

import com.f1soft.sces.entities.Course;
import com.f1soft.sces.entities.Enrollment;
import com.f1soft.sces.entities.Grade;
import com.f1soft.sces.entities.Student;
import com.f1soft.sces.enums.CompletionStatus;
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
