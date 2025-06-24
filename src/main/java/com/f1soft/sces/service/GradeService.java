package com.f1soft.sces.service;

import com.f1soft.sces.dto.GradePayload;
import com.f1soft.sces.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface GradeService {

  ResponseEntity<ResponseDto> getGradesForInstructor(String courseCode);

  ResponseEntity<ResponseDto> getGradesForStudent();

  ResponseEntity<ResponseDto> addGrade(GradePayload grades);
}
