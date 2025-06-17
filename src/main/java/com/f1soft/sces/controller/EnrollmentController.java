package com.f1soft.sces.controller;

import com.f1soft.sces.dto.EnrollmentPayload;
import com.f1soft.sces.dto.EnrollmentResponsePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

  private final EnrollmentService enrollmentService;

  @GetMapping("/list")
  public ResponseEntity<ResponseDto> getEnrollmentList() {
    return enrollmentService.getEnrollments();
  }

  @PostMapping("/enroll")
  public ResponseEntity<ResponseDto> addEnrollment(@RequestBody EnrollmentPayload payload) {
    return enrollmentService.addEnrollment(payload);
  }

  @PostMapping("/update")
  public ResponseEntity<ResponseDto> update(@RequestBody EnrollmentResponsePayload payload) {
    return enrollmentService.update(payload);
  }
  
}
