package com.example.mainBase.service;

import com.example.mainBase.dto.EnrollmentResponsePayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.entities.Student;
import com.example.mainBase.enums.EnrollStatus;
import org.springframework.http.ResponseEntity;

public interface StudentService {

  Student setEnrollStatus(EnrollStatus enrollStatus, EnrollmentResponsePayload payload);


  ResponseEntity<ResponseDto> getStudentsPerCourse(String code);
}
