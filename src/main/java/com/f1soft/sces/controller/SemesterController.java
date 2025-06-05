package com.f1soft.sces.controller;

import com.f1soft.sces.dto.SemesterDto;
import com.f1soft.sces.service.SemesterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/semester")
@RequiredArgsConstructor
public class SemesterController {

  private final SemesterService semesterService;

  @GetMapping("/getAll")
  public ResponseEntity<List<SemesterDto>> getAll() {
    return semesterService.getAllSemesters();
  }
}
