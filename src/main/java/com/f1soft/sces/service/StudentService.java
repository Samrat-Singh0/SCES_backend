package com.f1soft.sces.service;

import com.f1soft.sces.dto.EnrollmentResponsePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.entities.Student;
import com.f1soft.sces.enums.EnrollStatus;
import org.springframework.http.ResponseEntity;

public interface StudentService {

  Student setEnrollStatus(EnrollStatus enrollStatus, EnrollmentResponsePayload payload);


  ResponseEntity<ResponseDto> getStudentsPerCourse(String code);
}
