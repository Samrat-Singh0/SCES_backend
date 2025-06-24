package com.example.mainBase.service;

import com.example.mainBase.dto.GradePayload;
import com.example.mainBase.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface GradeService {

  ResponseEntity<ResponseDto> getGradesForInstructor(String courseCode);

  ResponseEntity<ResponseDto> getGradesForStudent();

  ResponseEntity<ResponseDto> addGrade(GradePayload grades);
}
