package com.f1soft.sces.service;

import com.example.attendance_fee_lib.enums.FeeStatus;
import com.f1soft.sces.builder.FeeBuilder;
import com.f1soft.sces.dto.FeePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.entities.Enrollment;
import com.f1soft.sces.entities.Fee;
import com.f1soft.sces.repository.EnrollmentRepository;
import com.f1soft.sces.repository.FeeRepository;
import com.f1soft.sces.util.ResponseBuilder;
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
