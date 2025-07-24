package com.example.mainBase.repository;

import com.example.mainBase.entities.SemesterCourse;
import org.springframework.data.repository.CrudRepository;

public interface SemesterCourseRepository extends CrudRepository<SemesterCourse, Long> {

  SemesterCourse findByCourseId(Long courseId);
}
