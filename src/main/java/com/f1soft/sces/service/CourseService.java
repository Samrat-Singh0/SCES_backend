package com.f1soft.sces.service;

import com.f1soft.sces.dto.CoursePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.model.FilterCourse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CourseService {

  ResponseEntity<ResponseDto> getCourse(String code);

  ResponseEntity<ResponseDto> getAllCourses();

  ResponseEntity<ResponseDto> getAllCourses(Pageable pageable);

  ResponseEntity<ResponseDto> getPendingCourses();

  ResponseEntity<ResponseDto> getCourseOnRoles();

  ResponseEntity<ResponseDto> addCourse(CoursePayload payload);

  ResponseEntity<ResponseDto> deleteCourse(String code, String remarks);

  ResponseEntity<ResponseDto> updateCourse(CoursePayload payload);

  ResponseEntity<ResponseDto> getCourseBySearchText(FilterCourse filterCriteria);

}
