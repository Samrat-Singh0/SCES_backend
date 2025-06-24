package com.example.mainBase.service;

import com.example.mainBase.builder.AttendanceBuilder;
import com.example.mainBase.dto.AttendancePayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.entities.Attendance;
import com.example.mainBase.entities.Course;
import com.example.mainBase.entities.Enrollment;
import com.example.mainBase.entities.Student;
import com.example.mainBase.entities.User;
import com.example.mainBase.enums.CompletionStatus;
import com.example.mainBase.enums.Role;
import com.example.mainBase.exception.ResourceNotFoundException;
import com.example.mainBase.mapper.AttendanceMapper;
import com.example.mainBase.repository.AttendanceRepository;
import com.example.mainBase.repository.CourseRepository;
import com.example.mainBase.repository.EnrollmentRepository;
import com.example.mainBase.repository.StudentRepository;
import com.example.mainBase.util.CommonBeanUtility;
import com.example.mainBase.util.ResponseBuilder;
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
