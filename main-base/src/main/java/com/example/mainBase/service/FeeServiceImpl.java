package com.example.mainBase.service;

import com.example.attendance_fee_lib.enums.FeeStatus;
import com.example.mainBase.builder.FeeBuilder;
import com.example.mainBase.dto.FeeAddPayload;
import com.example.mainBase.dto.FeeResponsePayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.entities.AuditLog;
import com.example.mainBase.entities.Enrollment;
import com.example.mainBase.entities.Fee;
import com.example.mainBase.entities.User;
import com.example.mainBase.enums.AuditAction;
import com.example.mainBase.exception.ResourceNotFoundException;
import com.example.mainBase.mapper.FeeMapper;
import com.example.mainBase.repository.EnrollmentRepository;
import com.example.mainBase.repository.FeeRepository;
import com.example.mainBase.util.CommonBeanUtility;
import com.example.mainBase.util.ResponseBuilder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeeServiceImpl implements FeeService {

  private final FeeRepository feeRepository;
  private final EnrollmentRepository enrollmentRepository;
  private final AuditLogService auditLogService;
  private final CommonBeanUtility commonBeanUtility;

  @Override
  public ResponseEntity<ResponseDto> getFeeHistory(String enrollmentCode) {
    Enrollment enrollment = Optional.ofNullable(
        enrollmentRepository.findByCode(enrollmentCode))
        .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
    List<Fee> feeList = feeRepository.findByEnrollmentId(enrollment.getId());

    List<FeeResponsePayload> feeResponsePayloadList = FeeMapper.INSTANCE.toPayloadList(feeList);

    for (int i = 0; i < feeList.size(); i++) {
      Fee fee = feeList.get(i);
      FeeResponsePayload payload = feeResponsePayloadList.get(i);

      AuditLog auditLog = auditLogService.getLog(AuditAction.CREATED,"Fee", fee.getId());
      System.out.println(auditLog.getActionTime());
      System.out.println(auditLog.getActionTime().toLocalDate());
      payload.setPaidDate(auditLog.getActionTime().toLocalDate());
    }

    return ResponseBuilder.success("Fetched fee history successfully", feeResponsePayloadList);
  }

  @Override
  public ResponseEntity<ResponseDto> addFee(FeeAddPayload payload) {

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

    User user = commonBeanUtility.getLoggedInUser();
    auditLogService.log(user, AuditAction.CREATED, "Fee", fee.getId());

    return ResponseBuilder.success("Fee Paid", null);
  }
}
