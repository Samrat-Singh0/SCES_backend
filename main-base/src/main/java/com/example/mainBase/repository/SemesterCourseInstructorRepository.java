package com.example.mainBase.repository;

import com.example.mainBase.entities.Instructor;
import com.example.mainBase.entities.SemesterCourseInstructor;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SemesterCourseInstructorRepository extends
    CrudRepository<SemesterCourseInstructor, Long> {

  List<SemesterCourseInstructor> findByInstructor(Instructor instructor);

  @Modifying
  @Query("DELETE FROM SemesterCourseInstructor  sci where sci.semesterCourse.id = :semesterCourseId")
  void deleteBySemesterCourse_Id(Long semesterCourseId);
}
