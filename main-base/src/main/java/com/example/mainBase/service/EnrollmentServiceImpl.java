package com.example.mainBase.service;

import com.example.attendance_fee_lib.services.LibFeeService;
import com.example.mainBase.builder.EnrollmentBuilder;
import com.example.mainBase.dto.CoursePayload;
import com.example.mainBase.dto.EnrollmentPayload;
import com.example.mainBase.dto.EnrollmentResponsePayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.entities.Course;
import com.example.mainBase.entities.Enrollment;
import com.example.mainBase.entities.Semester;
import com.example.mainBase.entities.Student;
import com.example.mainBase.entities.User;
import com.example.mainBase.enums.AuditAction;
import com.example.mainBase.enums.CompletionStatus;
import com.example.mainBase.enums.EnrollStatus;
import com.example.mainBase.enums.Role;
import com.example.mainBase.mapper.EnrollmentMapper;
import com.example.mainBase.model.FilterEnrollment;
import com.example.mainBase.repository.CourseRepository;
import com.example.mainBase.repository.EnrollmentRepository;
import com.example.mainBase.repository.SemesterRepository;
import com.example.mainBase.repository.StudentRepository;
import com.example.mainBase.specification.EnrollmentSpecification;
import com.example.mainBase.util.CommonBeanUtility;
import com.example.mainBase.util.ResponseBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

  private final EnrollmentRepository enrollmentRepository;
  private final StudentRepository studentRepository;
  private final SemesterRepository semesterRepository;
  private final CommonBeanUtility commonBeanUtility;
  private final CourseRepository courseRepository;
  private final AuditLogService auditLogService;
  private final LibFeeService libFeeService;


  @Override
  public ResponseEntity<ResponseDto> getEnrollments() {
      List<Enrollment> enrollments;
      User user = commonBeanUtility.getLoggedInUser();
      if (user.getRole() == Role.STUDENT) {
        Student student = studentRepository.findByUser(user);
        enrollments = enrollmentRepository.findEnrollmentForStudent(student.getId());
      } else {
        enrollments = enrollmentRepository.findByCompletionStatusNot(CompletionStatus.PENDING);
      }
      List<EnrollmentResponsePayload> responsePayloads = EnrollmentMapper.INSTANCE.toResponsePayloads(
          enrollments);
      return ResponseBuilder.success("Fetched Enrollments Successfully", responsePayloads);
  }

  @Override
  public ResponseEntity<ResponseDto> getEnrollments(Pageable pageable) {
    User user = commonBeanUtility.getLoggedInUser();
    Page<Enrollment> enrollmentPage;
    if (user.getRole() == Role.STUDENT) {
      Student student = studentRepository.findByUser(user);
      enrollmentPage = enrollmentRepository.findEnrollmentPageForStudent(student.getId(), pageable);
    }else {
      enrollmentPage = enrollmentRepository.findByCompletionStatusNot(CompletionStatus.PENDING, pageable);
    }
    Page<EnrollmentResponsePayload> responsePayloads = EnrollmentMapper.INSTANCE.toEnrollmentResponsePayloadPage(enrollmentPage);
    return ResponseBuilder.success("Fetched Enrollments Successfully", responsePayloads);
  }

  @Override
  public ResponseEntity<ResponseDto> getPendingEnrollments() {
    List<Enrollment> pendingEnrollments = enrollmentRepository.findByCompletionStatus(
        CompletionStatus.PENDING);

    return ResponseBuilder.success("Fetched Enrollments Successfully",
        EnrollmentMapper.INSTANCE.toResponsePayloads(pendingEnrollments));
  }

  @Override
  @Transactional
  public ResponseEntity<ResponseDto> addEnrollment(EnrollmentPayload payload) {
    try {
      Enrollment enrollment = EnrollmentMapper.INSTANCE.toEnrollment(payload);

      Optional<Semester> semester = semesterRepository.findByLabel(
          payload.getSemester().getLabel());
      if (semester.isEmpty()) {
        return ResponseBuilder.getFailedMessage("Semester Not Found");
      }

      User user = commonBeanUtility.getLoggedInUser();
      Student student = studentRepository.findByUser(user);
      if (student == null) {
        return ResponseBuilder.getFailedMessage("Student Not Found");
      }

      List<Enrollment> existingEnrollmentRunning = enrollmentRepository.findByStudent_idAndCompletionStatus(
          student.getId(),
          CompletionStatus.RUNNING);
      if (!existingEnrollmentRunning.isEmpty()) {
        return ResponseBuilder.getFailedMessage(
            "You are currently enrolled to an existing course.");
      }

      List<Enrollment> completedEnrollments = enrollmentRepository.findByStudent_idAndCompletionStatus(
          student.getId(), CompletionStatus.COMPLETED);

      if (!completedEnrollments.isEmpty()) {
        for (Enrollment completed : completedEnrollments) {
          boolean isOutstandingHigh = libFeeService.checkThreshold(semester.get().getFee(),
              completed.getOutstandingFee());
          if (isOutstandingHigh) {
            return ResponseBuilder.getFailedMessage(
                "You are not eligible to enroll due to high outstanding fee.");
          }
        }
      }

      boolean semesterCompleted = completedEnrollments.stream()
          .anyMatch(
              completed -> completed.getSemester().getLabel().equals(semester.get().getLabel()));

      if (semesterCompleted) {
        return ResponseBuilder.getFailedMessage("You have already completed this semester.");
      }

      List<Course> courses = new ArrayList<>();
      for (CoursePayload payload1 : payload.getCourses()) {
        Course course = courseRepository.findByCode(payload1.getCode());
        courses.add(course);
      }
      enrollment.setCourses(courses);

      List<Enrollment> existingEnrollment = enrollmentRepository.findAllByStudent_Id(
          student.getId());           //student may not be new
      if (existingEnrollment == null) {
        student.setEnrollStatus(EnrollStatus.NEW);
        studentRepository.save(student);
      }

      enrollmentRepository.save(EnrollmentBuilder.build(enrollment, semester.get(), student));

      auditLogService.log(user, AuditAction.CREATED, "Enrollment", enrollment.getId());

      return ResponseBuilder.success("Enrollment Added Successfully", null);
    } catch (Exception e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }
  }

  @Override
  @Transactional
  public ResponseEntity<ResponseDto> update(EnrollmentResponsePayload payload) {
    try {
      Enrollment existingEnrollment = enrollmentRepository.findByCode(
          payload.getCode());
      if (existingEnrollment == null) {
        return ResponseBuilder.getFailedMessage("Enrollment Not Found");
      }

      if (payload.getCompletionStatus().equals(CompletionStatus.COMPLETED)) {
        existingEnrollment.setCompletionDate(LocalDate.now());
      }

      existingEnrollment.setCompletionStatus(payload.getCompletionStatus());

      if (payload.getCompletionStatus().equals(
          CompletionStatus.RUNNING)) {                                                        //yedi update pending lai accept gareko ho vaye update student ko enrollStatus as well.
        Student student = studentRepository.findByCode(payload.getStudent().getCode());
        student.setEnrollStatus(EnrollStatus.ENROLLED);
        studentRepository.save(student);
        existingEnrollment.setStudent(student);
      }

      if (payload.getCompletionStatus().equals(CompletionStatus.REJECTED)) {
        Student student = studentRepository.findByCode(payload.getStudent().getCode());
        student.setEnrollStatus(EnrollStatus.REJECTED);
        studentRepository.save(student);
        existingEnrollment.setStudent(student);
      }

      enrollmentRepository.save(existingEnrollment);

      auditLogService.log(commonBeanUtility.getLoggedInUser(), AuditAction.UPDATED, "Enrollment",
          existingEnrollment.getId());

      return ResponseBuilder.success("Enrollment Updated Successfully", null);
    } catch (Exception e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }
  }

  @Override
  public ResponseEntity<ResponseDto> getCourseBySearchText(FilterEnrollment criteria,
      Pageable pageable) {

    Specification<Enrollment> specification = EnrollmentSpecification.buildSpec(criteria);

    Page<Enrollment> enrollmentPage = enrollmentRepository.findAll(specification, pageable);
    Page<EnrollmentResponsePayload> enrollmentResponsePayloads = EnrollmentMapper.INSTANCE.toEnrollmentResponsePayloadPage(enrollmentPage);

    return ResponseBuilder.success("Fetched Enrollment Successfully", enrollmentResponsePayloads);
  }
}
