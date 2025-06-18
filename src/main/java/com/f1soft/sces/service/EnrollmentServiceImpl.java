package com.f1soft.sces.service;

import com.f1soft.sces.builder.EnrollmentBuilder;
import com.f1soft.sces.dto.CoursePayload;
import com.f1soft.sces.dto.EnrollmentPayload;
import com.f1soft.sces.dto.EnrollmentResponsePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.entities.Course;
import com.f1soft.sces.entities.Enrollment;
import com.f1soft.sces.entities.Semester;
import com.f1soft.sces.entities.Student;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.enums.AuditAction;
import com.f1soft.sces.enums.CompletionStatus;
import com.f1soft.sces.enums.EnrollStatus;
import com.f1soft.sces.enums.Role;
import com.f1soft.sces.mapper.EnrollmentMapper;
import com.f1soft.sces.repository.CourseRepository;
import com.f1soft.sces.repository.EnrollmentRepository;
import com.f1soft.sces.repository.SemesterRepository;
import com.f1soft.sces.repository.StudentRepository;
import com.f1soft.sces.util.CommonBeanUtility;
import com.f1soft.sces.util.ResponseBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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


  @Override
  public ResponseEntity<ResponseDto> getEnrollments() {
    try {
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
    } catch (Exception e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }
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

      Enrollment existingEnrollmentRunning = enrollmentRepository.findByStudent_idAndCompletionStatus(
          student.getId(),
          CompletionStatus.RUNNING);
      if (existingEnrollmentRunning != null) {
        return ResponseBuilder.getFailedMessage(
            "You are currently enrolled to an existing course.");
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
}
