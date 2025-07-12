package com.example.mainBase.service;

import com.example.mainBase.dto.EnrollmentPayload;
import com.example.mainBase.dto.EnrollmentResponsePayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.model.FilterEnrollment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface EnrollmentService {

  ResponseEntity<ResponseDto> getEnrollments();

  ResponseEntity<ResponseDto> getEnrollments(Pageable pageable);

  ResponseEntity<ResponseDto> getPendingEnrollments();

  ResponseEntity<ResponseDto> addEnrollment(EnrollmentPayload payload);

  ResponseEntity<ResponseDto> update(EnrollmentResponsePayload payload);

  ResponseEntity<ResponseDto> getCourseBySearchText(FilterEnrollment criteria, Pageable pageable);
}
