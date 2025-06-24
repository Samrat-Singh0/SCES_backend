package com.example.mainBase.service;

import com.example.attendance_fee_lib.enums.FeeStatus;
import com.example.mainBase.builder.FeeBuilder;
import com.example.mainBase.dto.FeePayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.entities.Enrollment;
import com.example.mainBase.entities.Fee;
import com.example.mainBase.repository.EnrollmentRepository;
import com.example.mainBase.repository.FeeRepository;
import com.example.mainBase.util.ResponseBuilder;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeeServiceImpl implements FeeService {

  private final FeeRepository feeRepository;
  private final EnrollmentRepository enrollmentRepository;

  @Override
  public ResponseEntity<ResponseDto> addFee(FeePayload payload) {

    Enrollment enrollment = enrollmentRepository.findByCode(
        payload.getEnrollmentPayload().getCode());

    BigDecimal updatedFee = enrollment.getOutstandingFee().subtract(payload.getAmount());

    if (updatedFee.compareTo(BigDecimal.ZERO) < 0) {
      ResponseBuilder.getFailedMessage("You cannot pay more than outstanding amount");
    } else if (updatedFee.compareTo(BigDecimal.ZERO) == 0) {
      enrollment.setPaidStatus(FeeStatus.PAID);
    } else {
      enrollment.setPaidStatus(FeeStatus.PARTIALLY_PAID);
    }

    enrollment.setOutstandingFee(updatedFee);

    Fee fee = FeeBuilder.build(enrollment, payload);
    feeRepository.save(fee);

    return ResponseBuilder.success("Fee Paid", null);
  }
}
