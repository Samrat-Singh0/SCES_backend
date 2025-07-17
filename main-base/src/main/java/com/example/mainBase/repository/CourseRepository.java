package com.example.mainBase.repository;

import com.example.mainBase.dto.CourseReportDto;
import com.example.mainBase.entities.Course;
import com.example.mainBase.enums.ActiveStatus;
import com.example.mainBase.enums.Checked;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>,
    JpaSpecificationExecutor<Course> {

  Course findByCode(String code);

  List<Course> findByChecked(Checked checked);

  List<Course> findByCheckedAndActiveStatus(Checked checked, ActiveStatus active);

  Page<Course> findByCheckedAndActiveStatus(Checked checked, ActiveStatus active,
      Pageable pageable);

  Page<Course> findAll(Specification<Course> spec, Pageable pageable);

//  @Query(value = "select c from Course c where c.instructor.id=:instructorId and c.checked='CHECKED' and c.activeStatus='ACTIVE'")
//  List<Course> findByInstructorId(Long instructorId);

  @Query("""
          SELECT c from Course c 
          join AuditLog a on c.id = a.entityId 
          where a.entityName = 'Course' 
          and c.checked='PENDING'
      """)
  List<Course> fetchPendingCourses();

  @Query(value = "SELECT c from Course c "
      + "JOIN EnrollmentCourse ec ON ec.course.id = c.id "
      + "JOIN Enrollment e ON e.id=ec.enrollment.id "
      + "WHERE e.student.id = :studentId "
      + "AND e.completionStatus = 'RUNNING' "
      + "OR e.completionStatus='COMPLETED'")
  List<Course> fetchCourseForStudent(Long studentId);

  @Query(value = "SELECT c from Course c "
      + "WHERE NOT EXISTS ("
      + "SELECT sc FROM SemesterCourse sc WHERE sc.course = c) "
      + "AND c.checked='CHECKED' AND c.activeStatus='ACTIVE'")
  List<Course> fetchCourseWithNoSemester();

  @Query(value = "SELECT "
      + "c.name, "
      + "s.label, "
      + "COUNT(DISTINCT e.id) AS total_enrollments, "
      + "COUNT(DISTINCT CASE WHEN e.completion_status = 'RUNNING' THEN e.id END) AS running_enrollments, "
      + "AVG(g.grade) "
      + "FROM course c "
      + "LEFT JOIN enrollment_course ec ON c.id = ec.course_id "
      + "LEFT JOIN enrollment e ON e.id = ec.enrollment_id "
      + "LEFT JOIN grade g ON g.course_id = c.id "
      + "LEFT JOIN semester s ON s.id = c.semester_id "
      + "WHERE c.active_status='ACTIVE' "
      + "group by c.id"
      , nativeQuery = true)
  List<CourseReportDto> fetchCourseReports();                  //query returns a list of objects. tara entity use nagareko vayera JPA cannot map the objects fetched.

}
