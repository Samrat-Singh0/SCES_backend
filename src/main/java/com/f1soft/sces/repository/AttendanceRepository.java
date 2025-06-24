package com.f1soft.sces.repository;

import com.f1soft.sces.entities.Attendance;
import com.f1soft.sces.entities.Course;
import com.f1soft.sces.enums.CompletionStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

  @Query(value = "select a from Attendance a where a.date=:date and a.course.id=:courseId")
  List<Attendance> findByDateAndCourseId(LocalDate date, Long courseId);

  @Query(value = "select a from Attendance a "
      + "join Enrollment e "
      + "on a.enrollment.id=e.id "
      + "where a.student.id=:studentId "
      + "and a.date=:date "
      + "and e.completionStatus=:completionStatus")
  List<Attendance> findByStudentIdAndDate(Long studentId, LocalDate date,
      CompletionStatus completionStatus);


  Optional<Attendance> findByCourseAndDate(Course course, LocalDate date);
}
