package com.f1soft.sces.controller;

import com.f1soft.sces.dto.GradePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.service.GradeService;
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
@RequestMapping("/api/grade")
@RequiredArgsConstructor
public class GradeController {

  private final GradeService gradeService;

  @GetMapping("/list/student")
  @PreAuthorize("hasRole(@securityRoles.STUDENT)")
  ResponseEntity<ResponseDto> getGradeListStudent() {
    return this.gradeService.getGradesForStudent();
  }

  @GetMapping("/list/instructor/{courseCode}")
  @PreAuthorize("hasRole(@securityRoles.INSTRUCTOR)")
  ResponseEntity<ResponseDto> getGradeListInstructor(@PathVariable String courseCode) {
    return this.gradeService.getGradesForInstructor(courseCode);
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole(@securityRoles.INSTRUCTOR)")
  ResponseEntity<ResponseDto> addGrade(@RequestBody GradePayload payload) {
    return this.gradeService.addGrade(payload);
  }


}
