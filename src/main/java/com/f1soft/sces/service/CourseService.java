package com.f1soft.sces.service;

import com.f1soft.sces.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface CourseService {

  ResponseEntity<ResponseDto> getAllCourses();

}
