package com.example.mainBase.repository;

import com.example.mainBase.entities.Instructor;
import com.example.mainBase.entities.SemesterCourseInstructor;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface SemesterCourseInstructorRepository extends
    CrudRepository<SemesterCourseInstructor, Long> {

  List<SemesterCourseInstructor> findByInstructor(Instructor instructor);
}
