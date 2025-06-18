package com.f1soft.sces.controller;

import com.f1soft.sces.dto.CoursePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.model.FilterCourse;
import com.f1soft.sces.service.CourseService;
import lombok.RequiredArgsConstructor;
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
//  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> getCourses() {
    return courseService.getAllCourses();
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
