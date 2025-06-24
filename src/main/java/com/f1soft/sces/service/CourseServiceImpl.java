package com.f1soft.sces.service;

import com.f1soft.sces.dto.CoursePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.entities.Course;
import com.f1soft.sces.entities.Instructor;
import com.f1soft.sces.entities.Semester;
import com.f1soft.sces.entities.Student;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.enums.ActiveStatus;
import com.f1soft.sces.enums.AuditAction;
import com.f1soft.sces.enums.Checked;
import com.f1soft.sces.enums.Role;
import com.f1soft.sces.exception.ResourceNotFoundException;
import com.f1soft.sces.mapper.CourseMapper;
import com.f1soft.sces.mapper.UserMapper;
import com.f1soft.sces.model.FilterCourse;
import com.f1soft.sces.repository.AuditLogRepository;
import com.f1soft.sces.repository.CourseRepository;
import com.f1soft.sces.repository.InstructorRepository;
import com.f1soft.sces.repository.SemesterRepository;
import com.f1soft.sces.repository.StudentRepository;
import com.f1soft.sces.specification.CourseSpecification;
import com.f1soft.sces.util.CommonBeanUtility;
import com.f1soft.sces.util.CommonUtility;
import com.f1soft.sces.util.ResponseBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
  public ResponseEntity<ResponseDto> getCourseBySearchText(FilterCourse filterCriteria) {

    List<Course> courses = Optional.ofNullable(courseRepository.findByChecked(Checked.CHECKED))
        .orElseThrow(() -> new ResourceNotFoundException("Courses Not Found"));

    List<CoursePayload> courseResponseList;
    if (filterCriteria.getName() == null && filterCriteria.getInstructor() == null
        && filterCriteria.getSemester() == null) {
      courseResponseList = CourseMapper.INSTANCE.toCoursePayloadList(courses);
      return ResponseBuilder.success("Fetched Courses Successfully", courseResponseList);
    }

    Specification<Course> specification = CourseSpecification.buildSpec(filterCriteria);
    courseResponseList = courseRepository.findAll(specification).stream()
        .map(CourseMapper.INSTANCE::toCoursePayload).collect(Collectors.toList());

    return ResponseBuilder.success("Fetched Courses Successfully", courseResponseList);

  }


}
