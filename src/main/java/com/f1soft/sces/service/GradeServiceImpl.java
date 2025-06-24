package com.f1soft.sces.service;

import com.f1soft.sces.dto.GradePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.entities.Course;
import com.f1soft.sces.entities.Enrollment;
import com.f1soft.sces.entities.Grade;
import com.f1soft.sces.entities.Student;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.enums.AuditAction;
import com.f1soft.sces.enums.CompletionStatus;
import com.f1soft.sces.exception.ResourceNotFoundException;
import com.f1soft.sces.mapper.GradeMapper;
import com.f1soft.sces.repository.CourseRepository;
import com.f1soft.sces.repository.EnrollmentRepository;
import com.f1soft.sces.repository.GradeRepository;
import com.f1soft.sces.repository.StudentRepository;
import com.f1soft.sces.util.CommonBeanUtility;
import com.f1soft.sces.util.CommonUtility;
import com.f1soft.sces.util.ResponseBuilder;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

  private final GradeRepository gradeRepository;
  private final StudentRepository studentRepository;
  private final CommonBeanUtility commonBeanUtility;
  private final EnrollmentRepository enrollmentRepository;
  private final AuditLogService auditLogService;
  private final CourseRepository courseRepository;


  @Override
  public ResponseEntity<ResponseDto> getGradesForStudent() {
    User user = commonBeanUtility.getLoggedInUser();
    Student student = studentRepository.findByUser_Id(user.getId())
        .orElseThrow(() -> new ResourceNotFoundException("Student Not Found"));
    List<Grade> grades = gradeRepository.findByStudentId(student.getId(),
        CompletionStatus.RUNNING);

    List<GradePayload> payloads = GradeMapper.INSTANCE.toPayloads(grades);

    return ResponseBuilder.success("Fetched Grades Successfully", payloads);
  }

  @Override
  public ResponseEntity<ResponseDto> getGradesForInstructor(String courseCode) {
    List<Grade> grades = Optional.ofNullable(gradeRepository.fetchByCourseId(courseCode))
        .orElseThrow(() -> new ResourceNotFoundException("Course Not Found"));
    List<GradePayload> gradePayloads = GradeMapper.INSTANCE.toPayloads(grades);
    return ResponseBuilder.success("Fetched Grades Successfully", gradePayloads);

  }

  @Override
  public ResponseEntity<ResponseDto> addGrade(GradePayload payload) {

    Student student = studentRepository.findByCode(payload.getStudent().getCode());
    Course course = courseRepository.findByCode(payload.getCourse().getCode());
    List<Enrollment> enrollmentList = enrollmentRepository.findByStudent_idAndCompletionStatus(
        student.getId(),
        CompletionStatus.RUNNING);
    Enrollment enrollment = enrollmentList.getFirst();

    Optional<Grade> existingGrade = gradeRepository.findByStudentAndCourseAndEnrollment(student,
        course,
        enrollment);
    Grade grade;
    if (existingGrade.isEmpty()) {
      grade = Grade.builder()
          .code(CommonUtility.generateCode("GR-"))
          .course(course)
          .student(student)
          .enrollment(enrollment)
          .grade(payload.getGrade())
          .remark(payload.getRemark())
          .build();
    } else {
      grade = existingGrade.get();
      grade.setRemark(payload.getRemark());
      grade.setGrade(payload.getGrade());
    }

    gradeRepository.save(grade);
    auditLogService.log(commonBeanUtility.getLoggedInUser(), AuditAction.UPDATED, "Grade",
        grade.getId());

    return ResponseBuilder.success("Grades Added Successfully", null);

  }
}
