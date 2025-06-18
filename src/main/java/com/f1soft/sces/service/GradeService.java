package com.f1soft.sces.service;

import com.f1soft.sces.dto.GradePayload;
import com.f1soft.sces.dto.ResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface GradeService {

  ResponseEntity<ResponseDto> getGrades(GradePayload payload);

  ResponseEntity<ResponseDto> addGrade(List<GradePayload> grades);
}
