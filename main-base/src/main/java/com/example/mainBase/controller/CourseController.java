package com.example.mainBase.controller;

import com.example.mainBase.dto.CoursePayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.model.FilterCourse;
import com.example.mainBase.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/super/course")
public class CourseController {

  private final CourseService courseService;

  @GetMapping("/{code}")
  public ResponseEntity<ResponseDto> getCourse(@PathVariable String code) {
    return courseService.getCourse(code);
  }

  @GetMapping("/list")
  public ResponseEntity<ResponseDto> getCourses() {
    return courseService.getAllCourses();
  }

  @GetMapping("/paged/list")
  public ResponseEntity<ResponseDto> getCourses(
      @PageableDefault(size = 10) Pageable pageable
  ) {
    return courseService.getAllCourses(pageable);
  }

  @GetMapping("/list/pending")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> getPendingCourses() {
    return courseService.getPendingCourses();
  }

  @GetMapping("/list/role")
  public ResponseEntity<ResponseDto> getCourseRoles() {
    return courseService.getCourseOnRoles();
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> addCourse(@RequestBody CoursePayload course) {
    return courseService.addCourse(course);
  }

  @PostMapping("/delete/{code}")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> deleteCourse(@PathVariable String code,
      @RequestBody String remarks) {
    return courseService.deleteCourse(code, remarks);
  }

  @PostMapping("/update")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> updateCourse(@RequestBody CoursePayload course) {
    return courseService.updateCourse(course);
  }

  @PostMapping("/search")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> searchCourse(@RequestBody FilterCourse course) {
    return courseService.getCourseBySearchText(course);
  }

}
