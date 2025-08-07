package com.example.mainBase.controller;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.dto.SemesterPayload;
import com.example.mainBase.enums.ActiveStatus;
import com.example.mainBase.service.SemesterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/super/semester")
@RequiredArgsConstructor
public class SemesterController {

  private final SemesterService semesterService;

  @GetMapping("{label}")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> getSemester(
      @PathVariable String label) {
    return semesterService.getSemester(label);
  }

  @GetMapping("/list/{status}")
//  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> getAll(
      @PathVariable ActiveStatus status
  ) {
    return semesterService.getAllSemesters(status);
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> addSemester(
      @RequestBody @Valid SemesterPayload semesterPayload
  ) {
    return semesterService.addSemester(semesterPayload);
  }

  @PutMapping("/update")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> update(
      @RequestBody SemesterPayload semester) {
    return semesterService.updateSemester(semester);
  }

  @DeleteMapping("/delete/{label}")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> delete(@PathVariable String label) {
    return semesterService.deleteSemester(label);
  }
}
