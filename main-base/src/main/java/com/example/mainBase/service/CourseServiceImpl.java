package com.example.mainBase.service;

import com.example.mainBase.dto.CoursePayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.entities.Course;
import com.example.mainBase.entities.Instructor;
import com.example.mainBase.entities.Semester;
import com.example.mainBase.entities.Student;
import com.example.mainBase.entities.User;
import com.example.mainBase.enums.ActiveStatus;
import com.example.mainBase.enums.AuditAction;
import com.example.mainBase.enums.Checked;
import com.example.mainBase.enums.Role;
import com.example.mainBase.exception.ResourceNotFoundException;
import com.example.mainBase.mapper.CourseMapper;
import com.example.mainBase.mapper.UserMapper;
import com.example.mainBase.model.FilterCourse;
import com.example.mainBase.repository.AuditLogRepository;
import com.example.mainBase.repository.CourseRepository;
import com.example.mainBase.repository.InstructorRepository;
import com.example.mainBase.repository.SemesterRepository;
import com.example.mainBase.repository.StudentRepository;
import com.example.mainBase.specification.CourseSpecification;
import com.example.mainBase.util.CommonBeanUtility;
import com.example.mainBase.util.CommonUtility;
import com.example.mainBase.util.ResponseBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

  private final CourseRepository courseRepository;
  private final SemesterRepository semesterRepository;
  private final InstructorRepository instructorRepository;
  private final AuditLogService auditLogService;
  private final CommonBeanUtility commonBeanUtility;
  private final CommonBeanUtility commonUtility;
  private final AuditLogRepository auditLogRepository;
  private final StudentRepository studentRepository;

  @Override
  public ResponseEntity<ResponseDto> getCourse(String code) {
    Course course = Optional.ofNullable(courseRepository.findByCode(code))
        .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    CoursePayload coursePayload = CourseMapper.INSTANCE.toCoursePayload(course);
    return ResponseBuilder.success("Fetched Course Successfully", coursePayload);
  }

  @Override
  public ResponseEntity<ResponseDto> getAllCourses() {
    List<Course> courses = Optional.ofNullable(
            courseRepository.findByCheckedAndActiveStatus(Checked.CHECKED,
                ActiveStatus.ACTIVE))
        .orElseThrow(() -> new ResourceNotFoundException("Courses Not Found"));
    List<CoursePayload> courseResponseList = CourseMapper.INSTANCE.toCoursePayloadList(courses);
    return ResponseBuilder.success("Fetched Courses Successfully", courseResponseList);
  }

  @Override
  public ResponseEntity<ResponseDto> getAllCourses(Pageable pageable) {
    Page<Course> coursePage = courseRepository.findByCheckedAndActiveStatus(Checked.CHECKED,
        ActiveStatus.ACTIVE, pageable);
    Page<CoursePayload> coursePayloadPage = CourseMapper.INSTANCE.toCoursePayloadPage(coursePage);
    return ResponseBuilder.success("Fetched Courses Successfully", coursePayloadPage);
  }

  @Override
  public ResponseEntity<ResponseDto> getPendingCourses() {
    List<Course> courses = Optional.ofNullable(courseRepository.fetchPendingCourses(
            commonUtility.getLoggedInUser().getId()))
        .orElseThrow(() -> new ResourceNotFoundException("Courses Not Found"));
    List<CoursePayload> coursePayloads = new ArrayList<>();
    for (Course course : courses) {
      CoursePayload coursePayload = CourseMapper.INSTANCE.toCoursePayload(course);

      User user = auditLogRepository.getCreator(course.getId());
      coursePayload.setAddedBy(UserMapper.INSTANCE.toUserDto(user));

      coursePayloads.add(coursePayload);
    }
    return ResponseBuilder.success("Fetched Pending Courses Successfully", coursePayloads);
  }

  @Override
  public ResponseEntity<ResponseDto> getCourseOnRoles() {
    User currentUser = commonUtility.getLoggedInUser();

    if (currentUser.getRole().equals(Role.INSTRUCTOR)) {

      Instructor instructor = Optional.ofNullable(instructorRepository.findByUser(currentUser))
          .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
      List<Course> courses = Optional.ofNullable(
              courseRepository.findByInstructorId(instructor.getId()))
          .orElseThrow(() -> new ResourceNotFoundException("Courses Not Found"));
      return ResponseBuilder.success("Fetched Courses Successfully", courses);

    } else {

      Student student = Optional.ofNullable(studentRepository.findByUser(currentUser))
          .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
      List<Course> courses = Optional.ofNullable(
              courseRepository.fetchCourseForStudent(student.getId()))
          .orElseThrow(() -> new ResourceNotFoundException("Courses Not Found"));
      return ResponseBuilder.success("Fetched Courses Successfully", courses);

    }

  }

  @Override
  public ResponseEntity<ResponseDto> addCourse(CoursePayload coursePayload) {
    Course course = CourseMapper.INSTANCE.toCourse(coursePayload);

    String label = coursePayload.getSemester().getLabel();
    String instructorCode = coursePayload.getInstructor().getCode();

    Semester semester = semesterRepository.findByLabel(label)
        .orElseThrow(() -> new ResourceNotFoundException("Semester Not Found"));
    Instructor instructor = instructorRepository.findByCode(
        instructorCode).orElseThrow(() -> new ResourceNotFoundException("Instructor Not Found"));

    course.setCode(CommonUtility.generateCode("CR-"));
    course.setSemester(semester);
    course.setInstructor(instructor);
    course.setChecked(Checked.PENDING);
    course.setActiveStatus(ActiveStatus.ACTIVE);

    courseRepository.save(course);

    auditLogService.log(commonBeanUtility.getLoggedInUser(), AuditAction.CREATED, "Course",
        course.getId());

    return ResponseBuilder.success("Course Added Successfully", null);
  }

  @Override
  public ResponseEntity<ResponseDto> deleteCourse(String code, String remarks) {

    Course course = Optional.ofNullable(courseRepository.findByCode(code))
        .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

    course.setActiveStatus(ActiveStatus.INACTIVE);
    course.setRemarks(remarks);
    courseRepository.save(course);

    User loggedInUser = commonBeanUtility.getLoggedInUser();
    auditLogService.log(loggedInUser, AuditAction.DELETED, "Course", course.getId());

    return ResponseBuilder.success("Course Deleted Successfully", null);

  }

  @Override
  public ResponseEntity<ResponseDto> updateCourse(CoursePayload payload) {

    Course course = Optional.ofNullable(courseRepository.findByCode(payload.getCode()))
        .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

    course.setName(payload.getName());
    course.setCreditHours(payload.getCreditHours());
    course.setFullMarks(payload.getFullMarks());
    course.setChecked(payload.getChecked());

    Optional<Semester> optionalSemester = semesterRepository.findByLabel(
        payload.getSemester().getLabel());
    optionalSemester.ifPresent(course::setSemester);

    Optional<Instructor> optionalInstructor = instructorRepository.findByCode(
        payload.getInstructor().getCode());
    optionalInstructor.ifPresent(course::setInstructor);

    courseRepository.save(course);

    auditLogService.log(commonBeanUtility.getLoggedInUser(), AuditAction.UPDATED, "Course",
        course.getId());

    return ResponseBuilder.success("Course Updated Successfully", null);


  }

  @Override
  public ResponseEntity<ResponseDto> getCourseBySearchText(FilterCourse filterCriteria, Pageable pageable) {

    List<Course> courses = Optional.ofNullable(courseRepository.findByChecked(Checked.CHECKED))
        .orElseThrow(() -> new ResourceNotFoundException("Courses Not Found"));


    Specification<Course> specification = CourseSpecification.buildSpec(filterCriteria);

    Page<Course> coursePage = courseRepository.findAll(specification, pageable);
    Page<CoursePayload> courseResponseList = CourseMapper.INSTANCE.toCoursePayloadPage(coursePage);

    return ResponseBuilder.success("Fetched Courses Successfully", courseResponseList);

  }

}
