package com.example.mainBase.service;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.entities.Course;
import com.example.mainBase.entities.Student;
import com.example.mainBase.mapper.StudentMapper;
import com.example.mainBase.repository.CourseRepository;
import com.example.mainBase.repository.StudentRepository;
import com.example.mainBase.util.ResponseBuilder;
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
  public ResponseEntity<ResponseDto> getStudentsPerCourse(String code) {
    Course course = Optional.ofNullable(courseRepository.findByCode(code))
        .orElseThrow(() -> new RuntimeException("Course not found"));

    List<Student> students = Optional.ofNullable(studentRepository.findByCourse_Id(course.getId()))
        .orElseThrow(() -> new RuntimeException("Student not found"));

    return ResponseBuilder.success("Fetched Students successfully",
        StudentMapper.INSTANCE.toStudentPayloadList(students));

  }
}
