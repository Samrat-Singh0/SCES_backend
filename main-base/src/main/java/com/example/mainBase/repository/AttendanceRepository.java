package com.example.mainBase.repository;

import com.example.mainBase.entities.Attendance;
import com.example.mainBase.entities.Course;
import com.example.mainBase.enums.CompletionStatus;
import com.example.mainBase.model.AttendanceRate;
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

  @Query(value = "select "
      + "s.code, "
      + "sum(case when a.attendanceStatus='PRESENT' then 1 else 0 end ), "
      + "count(a) "
      + "from Attendance a "
      + "join Student s on a.student.id=s.id "
      + "where a.course.code=:courseCode "
      + "GROUP BY s.code")
  List<AttendanceRate> getAttendanceCountByCourse(String courseCode);
}
