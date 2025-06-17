package com.f1soft.sces.repository;

import com.f1soft.sces.entities.Course;
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

  @Query("""
          SELECT c from Course c join AuditLog a on c.id = a.entityId where a.entityName = 'Course' and a.user.id != :loggedInUserId and c.checked='PENDING'
      """)
  List<Course> fetchPendingCourses(long loggedInUserId);
}
