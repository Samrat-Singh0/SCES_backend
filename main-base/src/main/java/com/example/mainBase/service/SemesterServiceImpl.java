package com.example.mainBase.service;

import com.example.mainBase.dto.CoursePayload;
import com.example.mainBase.dto.InstructorPayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.dto.SemesterPayload;
import com.example.mainBase.entities.Course;
import com.example.mainBase.entities.Semester;
import com.example.mainBase.entities.SemesterCourse;
import com.example.mainBase.entities.SemesterCourseInstructor;
import com.example.mainBase.entities.User;
import com.example.mainBase.enums.AuditAction;
import com.example.mainBase.exception.ResourceNotFoundException;
import com.example.mainBase.mapper.CourseMapper;
import com.example.mainBase.mapper.InstructorMapper;
import com.example.mainBase.mapper.SemesterMapper;
import com.example.mainBase.repository.CourseRepository;
import com.example.mainBase.repository.InstructorRepository;
import com.example.mainBase.repository.SemesterRepository;
import com.example.mainBase.util.CommonBeanUtility;
import com.example.mainBase.util.ResponseBuilder;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {

  private final SemesterRepository semesterRepository;

  private final AuditLogService auditLogService;

  private final CommonBeanUtility commonBeanUtility;

  private final CourseRepository courseRepository;

  private final InstructorRepository instructorRepository;

  @Override
  public ResponseEntity<ResponseDto> getSemester(String label) {
    Optional<Semester> optionalSemester = semesterRepository.findByLabel(label);

    if (optionalSemester.isEmpty()) {
      return ResponseBuilder.getFailedMessage("Semester not found");
    }
    Semester semester = optionalSemester.get();
    SemesterPayload semesterResponse = SemesterMapper.INSTANCE.toSemesterResponsePayload(
        semester);
    return ResponseBuilder.success("Fetched Semester Successfully", semesterResponse);
  }

  @Override
  public ResponseEntity<ResponseDto> getAllSemesters() {
    try {
      SemesterMapper semesterMapper = SemesterMapper.INSTANCE;
      List<Semester> semesters = semesterRepository.findAll();

      List<SemesterPayload> semesterResponse = semesterMapper.toSemesterResponsePayloadList(
          semesters);

      for (int i = 0; i < semesters.size(); i++) {
        Semester semester = semesters.get(i);
        SemesterPayload payload = semesterResponse.get(i);

        List<CoursePayload> coursePayloads = semester.getSemesterCourses()
          .stream()
          .map(semesterCourse -> {
            CoursePayload coursePayload = CourseMapper.INSTANCE.toCoursePayload(semesterCourse.getCourse());

            List<InstructorPayload> instructors = semesterCourse.getSemesterCourseInstructors()
                .stream()
                .map(instructor -> InstructorMapper.INSTANCE.toInstructorPayload(instructor.getInstructor()))
                .toList();

            coursePayload.setInstructors(instructors);
            return coursePayload;
          })
            .toList();


        payload.setCourse(coursePayloads);

      }

      return ResponseBuilder.success("Fetched Semester List Successfully.", semesterResponse);
    } catch (Exception error) {
      return ResponseBuilder.getFailedMessage(error.getMessage());
    }

  }

  @Override
  @Transactional
  public ResponseEntity<ResponseDto> addSemester(SemesterPayload payload) {

    Optional<Semester> optionalSemester = semesterRepository.findByLabel(payload.getLabel());
    if (optionalSemester.isPresent()) {
      return ResponseBuilder.getFailedMessage("Semester already exists");
    }
    Semester semester = SemesterMapper.INSTANCE.toSemester(payload);

    List<SemesterCourse>  semesterCourses = payload.getCourse().stream()
        .map(coursePayload -> {
          Course course = Optional.ofNullable(courseRepository.findByCode(coursePayload.getCode()))
              .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

          SemesterCourse semesterCourse = new SemesterCourse();
          semesterCourse.setCourse(course);
          semesterCourse.setSemester(semester);

          List<SemesterCourseInstructor> instructors = coursePayload.getInstructors().stream()
              .map(instructorPayload -> SemesterCourseInstructor.builder()
                  .semesterCourse(semesterCourse)
                  .instructor(instructorRepository.findByCode(instructorPayload.getCode())
                      .orElseThrow(() -> new ResourceNotFoundException("Instructor not found")))
                  .build())
              .toList();

          semesterCourse.setSemesterCourseInstructors(instructors);

          return semesterCourse;
        }).toList();
    semester.setSemesterCourses(semesterCourses);

    semesterRepository.save(semester);
    User user = commonBeanUtility.getLoggedInUser();
    auditLogService.log(user, AuditAction.CREATED, "Semester", semester.getId());
    return ResponseBuilder.success("Added Semester Successfully", SemesterMapper.INSTANCE.toSemesterResponsePayload(semester));
  }

  @Override
  @Transactional
  public ResponseEntity<ResponseDto> updateSemester(
    SemesterPayload semesterPayload) {
    Optional<Semester> optionalSemester = semesterRepository.findByLabel(
        semesterPayload.getLabel());
    if (optionalSemester.isEmpty()) {
      return ResponseBuilder.getFailedMessage("Semester not found.");
    }

    Semester semester = optionalSemester.get();

    semester.setFee(semesterPayload.getFee());
    semester.setStartDate(semesterPayload.getStartDate());
    semester.setEndDate(semesterPayload.getEndDate());

    semester.getSemesterCourses().clear();
    semesterRepository.flush();
    List<SemesterCourse> newSemesterCourses = semesterPayload.getCourse().stream()
        .map(coursePayload -> {
          Course course = Optional.ofNullable(courseRepository.findByCode(coursePayload.getCode()))
              .orElseThrow(() -> new RuntimeException("Course not found: " + coursePayload.getCode()));

          return SemesterCourse.builder()
              .semester(semester)
              .course(course)
              .build();
        })
        .toList();

    semester.getSemesterCourses().addAll(newSemesterCourses);
    semester.setSemesterCourses(newSemesterCourses);
    try{
      semesterRepository.save(semester);
    }catch (Exception error){
      throw new RuntimeException(error);
    }

      User user = commonBeanUtility.getLoggedInUser();
      auditLogService.log(user, AuditAction.UPDATED, "Semester", semester.getId());

    return ResponseBuilder.success("Semester Updated.", null);
  }

  @Override
  public ResponseEntity<ResponseDto> deleteSemester(String label) {
    Optional<Semester> semester = semesterRepository.findByLabel(label);
    if (semester.isEmpty()) {
      return ResponseBuilder.getFailedMessage("Semester not found.");
    }
    semesterRepository.delete(semester.get());
    return ResponseBuilder.success("Semester Deleted.", null);
  }


}
