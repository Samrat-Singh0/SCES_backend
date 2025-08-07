package com.example.mainBase.dto;

import com.example.attendance_fee_lib.enums.FeeStatus;
import com.example.mainBase.enums.CompletionStatus;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor

public class EnrollmentResponsePayload extends EnrollmentPayload {

  @NotBlank(message = "Code is required")
  private String code;
  private StudentPayload student;
  private CompletionStatus completionStatus;
  private LocalDate enrolledDate;
  private LocalDate completionDate;
  private BigDecimal outstandingFee;
  private List<CoursePayload> courses;
  private FeeStatus paidStatus;
}
