package com.example.mainBase.service;

import com.example.mainBase.dto.CoursePayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.model.FilterCourse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CourseService {

  ResponseEntity<ResponseDto> getCourse(String code);

  ResponseEntity<ResponseDto> getAllCourses();

  ResponseEntity<ResponseDto> getAllCourses(Pageable pageable);

  ResponseEntity<ResponseDto> getPendingCourses();

  ResponseEntity<ResponseDto> getCourseOnRoles();

  ResponseEntity<ResponseDto> getCoursesWithNoSemester();

  ResponseEntity<ResponseDto> addCourse(CoursePayload payload);

  ResponseEntity<ResponseDto> deleteCourse(String code, String remarks);

  ResponseEntity<ResponseDto> updateCourse(CoursePayload payload);

  ResponseEntity<ResponseDto> getCourseBySearchText(FilterCourse filterCourse, Pageable pageable);

}
