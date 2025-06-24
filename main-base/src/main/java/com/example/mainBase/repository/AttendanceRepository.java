package com.example.mainBase.repository;

import com.example.mainBase.entities.Attendance;
import com.example.mainBase.entities.Course;
import com.example.mainBase.enums.CompletionStatus;
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
