package com.example.mainBase.repository;

import com.example.mainBase.entities.SemesterCourse;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SemesterCourseRepository extends CrudRepository<SemesterCourse, Long> {

  SemesterCourse findByCourseId(Long courseId);

  @Modifying
  @Query("DELETE FROM SemesterCourse sc WHERE sc.id = :id")
  void deleteById(Long id);
}
