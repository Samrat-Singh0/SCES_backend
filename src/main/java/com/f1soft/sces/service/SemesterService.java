package com.f1soft.sces.service;

import com.f1soft.sces.dto.SemesterDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface SemesterService {
  ResponseEntity<List<SemesterDto>> getAllSemesters();
}
