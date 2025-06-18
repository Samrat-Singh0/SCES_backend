package com.f1soft.sces.controller;

import com.f1soft.sces.dto.GradePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.service.GradeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grade")
@RequiredArgsConstructor
public class GradeController {

  private final GradeService gradeService;

  @GetMapping("/list")
  ResponseEntity<ResponseDto> getGradeList(@RequestBody GradePayload payload) {
    return this.gradeService.getGrades(payload);
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole(@securityRoles.INSTRUCTOR)")
  ResponseEntity<ResponseDto> addGrade(@RequestBody List<GradePayload> payload) {
    return this.gradeService.addGrade(payload);
  }
}
