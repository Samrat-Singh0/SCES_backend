package com.example.reportingAndAnalytics.anaytics.repository;

import com.example.mainBase.repository.EnrollmentRepository;
import com.example.reportingAndAnalytics.anaytics.model.AnalyticDto;
import org.springframework.data.jpa.repository.Query;

public interface AnalyticsRepository extends EnrollmentRepository {

//  @Query(value = "SELECT count(course_id), count(student_id), count(instructor_id)  from enrollment e "
//      + "LEFT JOIN enrollment_course ec ON e.id=ec.enrollment_id "
//      + "LEFT JOIN course c ON c.id = ec.course_id "
//      + "LEFT JOIN student s ON s.id=e.student_id "
//      + "LEFT JOIN user u ON u.id=s.user_id "
//      + "LEFT JOIN instructor i ON i.user_id=u.id "
//      + "WHERE s.enroll_status='ENROLLED' AND s.enroll_status='GRADUATED' "
//      + "AND u.active_status='ACTIVE' "
//      + "AND c.active_status='ACTIVE' "
//      , nativeQuery = true)
//  List<AnalyticDto> getAllAnalytics();

  @Query(value = "SELECT "
      + "(SELECT COUNT(c) FROM Course c WHERE c.activeStatus='ACTIVE' AND c.checked='CHECKED') AS totalCourse,"
      + "(SELECT COUNT(u) FROM User u WHERE u.role='STUDENT' AND u.activeStatus='ACTIVE' ) AS totalStudent,"
      + "(SELECT COUNT(u) FROM User u WHERE u.role='INSTRUCTOR' AND u.activeStatus='ACTIVE') AS totalInstructor,"
      + "(SELECT SUM(s.fee) FROM Semester s),"
      + "(SELECT SUM(e.outstandingFee) FROM Enrollment e)")
  AnalyticDto getAnalyticsList();
}
