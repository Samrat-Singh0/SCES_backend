package com.example.mainBase.controller;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/super/instructor")
@RequiredArgsConstructor
public class InstructorController {

  private final InstructorService instructorService;

  @GetMapping("/list")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> getInstructors() {
    return instructorService.getAllInstructors();
  }
}
