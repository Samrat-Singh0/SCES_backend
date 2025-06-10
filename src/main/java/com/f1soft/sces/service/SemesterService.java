package com.f1soft.sces.service;

import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.dto.SemesterPayload;
import org.springframework.http.ResponseEntity;

public interface SemesterService {

  ResponseEntity<ResponseDto> getSemester(String label);

  ResponseEntity<ResponseDto> getAllSemesters();

  ResponseEntity<ResponseDto> addSemester(SemesterPayload payload);

  ResponseEntity<ResponseDto> updateSemester(
      SemesterPayload semesterPayload);

  ResponseEntity<ResponseDto> deleteSemester(String label);
}
