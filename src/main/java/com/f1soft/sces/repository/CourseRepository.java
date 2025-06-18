package com.f1soft.sces.repository;

import com.f1soft.sces.entities.Course;
import com.f1soft.sces.entities.Instructor;
import com.f1soft.sces.enums.ActiveStatus;
import com.f1soft.sces.enums.Checked;
import java.util.List;
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

  List<Course> findByInstructor(Instructor instructor);

  @Query("""
          SELECT c from Course c join AuditLog a on c.id = a.entityId where a.entityName = 'Course' and a.user.id != :loggedInUserId and c.checked='PENDING'
      """)
  List<Course> fetchPendingCourses(long loggedInUserId);

  @Query(value = "SELECT c from Course c "
      + "JOIN EnrollmentCourse ec ON ec.course.id = c.id "
      + "JOIN Enrollment e ON e.id=ec.enrollment.id "
      + "WHERE e.student.id = :studentId "
      + "AND e.completionStatus = 'RUNNING' "
      + "AND e.completionStatus='COMPLETED'")
  List<Course> fetchCourseForStudent(Long studentId);
}
