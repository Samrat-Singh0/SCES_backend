package com.example.mainBase.service;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.dto.SemesterPayload;
import org.springframework.http.ResponseEntity;

public interface SemesterService {

  ResponseEntity<ResponseDto> getSemester(String label);

  ResponseEntity<ResponseDto> getAllSemesters();

  ResponseEntity<ResponseDto> addSemester(SemesterPayload payload);

  ResponseEntity<ResponseDto> updateSemester(
      SemesterPayload semesterPayload);

  ResponseEntity<ResponseDto> deleteSemester(String label);
}
