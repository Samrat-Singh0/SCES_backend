package com.f1soft.sces.controller;

import com.f1soft.sces.dto.CoursePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/super/course")
public class CourseController {

  private final CourseService courseService;

  @GetMapping("/list")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> getCourses() {
    return courseService.getAllCourses();
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> addCourse(@RequestBody CoursePayload course) {
    return courseService.addCourse(course);
  }
}
