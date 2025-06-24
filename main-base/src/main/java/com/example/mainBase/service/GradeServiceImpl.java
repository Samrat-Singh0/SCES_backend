package com.example.mainBase.service;

import com.example.mainBase.dto.GradePayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.entities.Course;
import com.example.mainBase.entities.Enrollment;
import com.example.mainBase.entities.Grade;
import com.example.mainBase.entities.Student;
import com.example.mainBase.entities.User;
import com.example.mainBase.enums.AuditAction;
import com.example.mainBase.enums.CompletionStatus;
import com.example.mainBase.exception.ResourceNotFoundException;
import com.example.mainBase.mapper.GradeMapper;
import com.example.mainBase.repository.CourseRepository;
import com.example.mainBase.repository.EnrollmentRepository;
import com.example.mainBase.repository.GradeRepository;
import com.example.mainBase.repository.StudentRepository;
import com.example.mainBase.util.CommonBeanUtility;
import com.example.mainBase.util.CommonUtility;
import com.example.mainBase.util.ResponseBuilder;
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
