package com.f1soft.sces.service;

import com.f1soft.sces.dto.EnrollmentPayload;
import com.f1soft.sces.dto.EnrollmentResponsePayload;
import com.f1soft.sces.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface EnrollmentService {

  ResponseEntity<ResponseDto> getEnrollments();

  ResponseEntity<ResponseDto> getPendingEnrollments();

  ResponseEntity<ResponseDto> addEnrollment(EnrollmentPayload payload);

  ResponseEntity<ResponseDto> update(EnrollmentResponsePayload payload);
}
