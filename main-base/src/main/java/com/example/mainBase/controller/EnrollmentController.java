package com.example.mainBase.controller;

import com.example.mainBase.dto.EnrollmentPayload;
import com.example.mainBase.dto.EnrollmentResponsePayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.model.FilterEnrollment;
import com.example.mainBase.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @PostMapping("/paged/list")
  public ResponseEntity<ResponseDto> getEnrollmentListPaged(
      @PageableDefault(size = 5, sort = "id", direction = Direction.DESC)
      @RequestBody FilterEnrollment filterEnrollment,
      Pageable pageable) {
    return enrollmentService.getEnrollments(filterEnrollment, pageable);
  }

  @GetMapping("/list/pending")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> getPendingEnrollmentList() {
    return enrollmentService.getPendingEnrollments();
  }

  @PostMapping("/enroll")
  public ResponseEntity<ResponseDto> addEnrollment(@RequestBody @Valid EnrollmentPayload payload) {
    return enrollmentService.addEnrollment(payload);
  }

  @PostMapping("/update")
  public ResponseEntity<ResponseDto> update(@RequestBody @Valid EnrollmentResponsePayload payload) {
    return enrollmentService.update(payload);
  }

}
