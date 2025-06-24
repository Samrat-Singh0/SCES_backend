package com.f1soft.sces.controller;

import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/instructor/")
@RequiredArgsConstructor
public class StudentController {

  private final StudentService studentService;

  @GetMapping("/list/student/{code}")
  @PreAuthorize("hasRole(@securityRoles.INSTRUCTOR)")
  public ResponseEntity<ResponseDto> getStudentsPerCourse(@PathVariable String code) {
    return studentService.getStudentsPerCourse(code);
  }
}
