package com.f1soft.sces.service;

import com.f1soft.sces.dto.GradePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.entities.Enrollment;
import com.f1soft.sces.entities.Grade;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.enums.AuditAction;
import com.f1soft.sces.enums.CompletionStatus;
import com.f1soft.sces.enums.Role;
import com.f1soft.sces.mapper.GradeMapper;
import com.f1soft.sces.repository.EnrollmentRepository;
import com.f1soft.sces.repository.GradeRepository;
import com.f1soft.sces.util.CommonBeanUtility;
import com.f1soft.sces.util.CommonUtility;
import com.f1soft.sces.util.ResponseBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

  private final GradeRepository gradeRepository;
  private final CommonBeanUtility commonBeanUtility;
  private final EnrollmentRepository enrollmentRepository;
  private final AuditLogService auditLogService;

  @Override
  public ResponseEntity<ResponseDto> getGrades(GradePayload grade) {
    User user = commonBeanUtility.getLoggedInUser();
    if (user.getRole().equals(Role.INSTRUCTOR)) {

    } else {

    }
    return null;
  }

  @Override
  public ResponseEntity<ResponseDto> addGrade(List<GradePayload> payloads) {
    try {
      for (GradePayload payload : payloads) {
        Grade grade = GradeMapper.INSTANCE.toGrade(payload);
        Enrollment enrollment = enrollmentRepository.findByCompletionStatusRunning(
            CompletionStatus.RUNNING);
        grade.setCode(CommonUtility.generateCode("GR-"));
        grade.setEnrollment(enrollment);
        gradeRepository.save(grade);
        auditLogService.log(commonBeanUtility.getLoggedInUser(), AuditAction.CREATED, "Grade",
            grade.getId());
      }

      return ResponseBuilder.success("Grades Added Successfully", null);

    } catch (Exception e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }
  }
}
