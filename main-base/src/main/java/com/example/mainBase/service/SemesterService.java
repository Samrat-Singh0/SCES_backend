package com.example.mainBase.service;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.dto.SemesterPayload;
import com.example.mainBase.enums.ActiveStatus;
import org.springframework.http.ResponseEntity;

public interface SemesterService {

  ResponseEntity<ResponseDto> getSemester(String label);

  ResponseEntity<ResponseDto> getAllSemesters(ActiveStatus activeStatus);

  ResponseEntity<ResponseDto> addSemester(SemesterPayload payload);

  ResponseEntity<ResponseDto> updateSemester(
      SemesterPayload semesterPayload);

  ResponseEntity<ResponseDto> deleteSemester(String label);
}
