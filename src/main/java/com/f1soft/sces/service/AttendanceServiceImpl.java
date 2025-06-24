package com.f1soft.sces.service;

import com.f1soft.sces.builder.AttendanceBuilder;
import com.f1soft.sces.dto.AttendancePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.entities.Attendance;
import com.f1soft.sces.entities.Course;
import com.f1soft.sces.entities.Enrollment;
import com.f1soft.sces.entities.Student;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.enums.CompletionStatus;
import com.f1soft.sces.enums.Role;
import com.f1soft.sces.exception.ResourceNotFoundException;
import com.f1soft.sces.mapper.AttendanceMapper;
import com.f1soft.sces.repository.AttendanceRepository;
import com.f1soft.sces.repository.CourseRepository;
import com.f1soft.sces.repository.EnrollmentRepository;
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

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

  private final EnrollmentRepository enrollmentRepository;
  private final StudentRepository studentRepository;
  private final CourseRepository courseRepository;
  private final AttendanceRepository attendanceRepository;
  private final CommonBeanUtility commonBeanUtility;


  @Override
  public ResponseEntity<ResponseDto> getAttendanceOfDate(String courseCode, String date) {
    User user = commonBeanUtility.getLoggedInUser();

    if (user.getRole().equals(Role.INSTRUCTOR)) {
      Course course = Optional.ofNullable(courseRepository.findByCode(courseCode))
          .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

      List<Attendance> attendanceList = attendanceRepository.findByDateAndCourseId(
          LocalDate.parse(date),
          course.getId());

      return ResponseBuilder.success("Fetched Today's Attendance",
          AttendanceMapper.INSTANCE.toAttendancePayloadList(attendanceList));
    } else {
      Student student = studentRepository.findByUser_Id(user.getId())
          .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
      List<Attendance> attendances = attendanceRepository.findByStudentIdAndDate(student.getId(),
          LocalDate.parse(date),
          CompletionStatus.RUNNING);

      List<AttendancePayload> payloads = AttendanceMapper.INSTANCE.toAttendancePayloadList(
          attendances);

      return ResponseBuilder.success("Fetched Attendances Successfully", payloads);
    }
  }

  @Override
  public ResponseEntity<ResponseDto> addAttendance(List<AttendancePayload> payloads) {
    List<Attendance> attendances = new ArrayList<>();

    for (AttendancePayload payload : payloads) {
      Student student = Optional.ofNullable(
              studentRepository.findByCode(payload.getStudent().getCode()))
          .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
      Course course = Optional.of(courseRepository.findByCode(payload.getCourse().getCode()))
          .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
      List<Enrollment> enrollmentList = enrollmentRepository.findByStudent_idAndCompletionStatus(
          student.getId(), CompletionStatus.RUNNING);

      Enrollment enrollment = enrollmentList.getFirst();

      Optional<Attendance> existingAttendance = attendanceRepository.findByCourseAndDate(course,
          LocalDate.now());
      if (existingAttendance.isPresent()) {
        return ResponseBuilder.getFailedMessage("Attendance has already been done");
      }
      Attendance newAttendance = AttendanceBuilder.build(payload, student, course, enrollment);
      attendances.add(newAttendance);
    }

    attendanceRepository.saveAll(attendances);
    return ResponseBuilder.success(
        "Attendance Done.",
        null);
  }

}
