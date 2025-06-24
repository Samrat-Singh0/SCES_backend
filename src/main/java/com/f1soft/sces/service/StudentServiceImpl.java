package com.f1soft.sces.service;

import com.f1soft.sces.dto.EnrollmentResponsePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.entities.Course;
import com.f1soft.sces.entities.Student;
import com.f1soft.sces.enums.CompletionStatus;
import com.f1soft.sces.enums.EnrollStatus;
import com.f1soft.sces.mapper.StudentMapper;
import com.f1soft.sces.repository.CourseRepository;
import com.f1soft.sces.repository.StudentRepository;
import com.f1soft.sces.util.ResponseBuilder;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

  private final StudentRepository studentRepository;
  private final CourseRepository courseRepository;

  @Override
  public Student setEnrollStatus(EnrollStatus enrollStatus, EnrollmentResponsePayload payload) {
    if (payload.getCompletionStatus().equals(
        CompletionStatus.RUNNING)) {                                                        //yedi update pending lai accept gareko ho vaye update student ko enrollStatus as well.
      Student student = studentRepository.findByCode(payload.getStudent().getCode());
      student.setEnrollStatus(EnrollStatus.ENROLLED);
      studentRepository.save(student);
      return student;
    }

    if (payload.getCompletionStatus().equals(CompletionStatus.REJECTED)) {
      Student student = studentRepository.findByCode(payload.getStudent().getCode());
      student.setEnrollStatus(EnrollStatus.REJECTED);
      studentRepository.save(student);
      return student;
    }
    return null;
  }

  @Override
  public ResponseEntity<ResponseDto> getStudentsPerCourse(String code) {
    Course course = Optional.ofNullable(courseRepository.findByCode(code))
        .orElseThrow(() -> new RuntimeException("Course not found"));

    List<Student> students = Optional.ofNullable(studentRepository.findByCourse_Id(course.getId()))
        .orElseThrow(() -> new RuntimeException("Student not found"));

    return ResponseBuilder.success("Fetched Students successfully",
        StudentMapper.INSTANCE.toStudentPayloadList(students));

  }
}
