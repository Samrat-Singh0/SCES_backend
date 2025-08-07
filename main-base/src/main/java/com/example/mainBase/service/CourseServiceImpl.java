package com.example.mainBase.service;

import com.example.mainBase.dto.CoursePayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.entities.Course;
import com.example.mainBase.entities.Enrollment;
import com.example.mainBase.entities.Instructor;
import com.example.mainBase.entities.Semester;
import com.example.mainBase.entities.SemesterCourse;
import com.example.mainBase.entities.SemesterCourseInstructor;
import com.example.mainBase.entities.Student;
import com.example.mainBase.entities.User;
import com.example.mainBase.enums.ActiveStatus;
import com.example.mainBase.enums.AuditAction;
import com.example.mainBase.enums.Checked;
import com.example.mainBase.enums.Role;
import com.example.mainBase.exception.ResourceNotFoundException;
import com.example.mainBase.mapper.CourseMapper;
import com.example.mainBase.mapper.SemesterMapper;
import com.example.mainBase.mapper.UserMapper;
import com.example.mainBase.model.FilterCourse;
import com.example.mainBase.repository.AuditLogRepository;
import com.example.mainBase.repository.CourseRepository;
import com.example.mainBase.repository.EnrollmentRepository;
import com.example.mainBase.repository.InstructorRepository;
import com.example.mainBase.repository.SemesterCourseInstructorRepository;
import com.example.mainBase.repository.SemesterCourseRepository;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

  private final CourseRepository courseRepository;
  private final SemesterCourseInstructorRepository semesterCourseInstructorRepository;
  private final SemesterCourseRepository semesterCourseRepository;
  private final InstructorRepository instructorRepository;
  private final AuditLogService auditLogService;
  private final CommonBeanUtility commonBeanUtility;
  private final CommonBeanUtility commonUtility;
  private final AuditLogRepository auditLogRepository;
  private final StudentRepository studentRepository;
  private final EnrollmentRepository enrollmentRepository;

  @Override
  public ResponseEntity<ResponseDto> getCourse(String code) {
    Course course = Optional.ofNullable(courseRepository.findByCode(code))
        .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    CoursePayload coursePayload = CourseMapper.INSTANCE.toCoursePayload(course);

    SemesterCourse semesterCourse = semesterCourseRepository.findByCourseId(course.getId());
    if(semesterCourse != null){
      Semester semester = semesterCourse.getSemester();
      coursePayload.setSemester(SemesterMapper.INSTANCE.toSemesterResponsePayload(semester));
    }

    return ResponseBuilder.success("Fetched Course Successfully", coursePayload);
  }

  @Override
  public ResponseEntity<ResponseDto> getAllCourses() {
    List<Course> courses = Optional.ofNullable(
            courseRepository.findByCheckedAndActiveStatus(Checked.CHECKED,
                ActiveStatus.ACTIVE))
        .orElseThrow(() -> new ResourceNotFoundException("Courses Not Found"));
    List<CoursePayload> coursePayloads = setSemesterCourse(courses);
    return ResponseBuilder.success("Fetched Courses Successfully", coursePayloads);
  }

  private List<CoursePayload> setSemesterCourse(List<Course> courses) {
    List<CoursePayload> coursePayloads = new ArrayList<>();
    for (Course course : courses) {
      SemesterCourse semesterCourse = semesterCourseRepository.findByCourseId(course.getId());
      CoursePayload coursePayload = CourseMapper.INSTANCE.toCoursePayload(course);
      if (semesterCourse != null && semesterCourse.getSemester() != null) {
        coursePayload.setSemester(
            SemesterMapper.INSTANCE.toSemesterResponsePayload(semesterCourse.getSemester()));
      }
      coursePayloads.add(coursePayload);
    }
    return coursePayloads;
  }

  @Override
  public ResponseEntity<ResponseDto> getAllCourses(FilterCourse criteria, Pageable pageable) {

    Page<Course> coursePage = courseRepository.findByCheckedAndActiveStatus(Checked.CHECKED,
        ActiveStatus.ACTIVE, pageable);
    List<CoursePayload> coursePayloadList = setSemesterCourse(coursePage.getContent());
    Page<CoursePayload> coursePayloadPage = new PageImpl<>(coursePayloadList, pageable,
        coursePage.getTotalElements());

    if (criteria.getInstructor() != null || criteria.getName() != null
        || criteria.getSemester() != null) {
      Specification<Course> specification = CourseSpecification.buildSpec(criteria);

      Page<Course> filteredCoursePage = courseRepository.findAll(specification, pageable);
      List<CoursePayload> filteredCoursePayloadList = setSemesterCourse(filteredCoursePage.getContent());
      coursePayloadPage = new PageImpl<>(filteredCoursePayloadList, pageable, filteredCoursePage.getTotalElements());

    }
    return ResponseBuilder.success("Fetched Courses Successfully", coursePayloadPage);
  }

  @Override
  public ResponseEntity<ResponseDto> getPendingCourses() {
    List<Course> courses = Optional.ofNullable(courseRepository.fetchPendingCourses())
        .orElseThrow(() -> new ResourceNotFoundException("Courses Not Found"));
    List<CoursePayload> coursePayloads = new ArrayList<>();
    for (Course course : courses) {
      CoursePayload coursePayload = CourseMapper.INSTANCE.toCoursePayload(course);
      SemesterCourse semesterCourse = semesterCourseRepository.findByCourseId(course.getId());
      if (semesterCourse != null && semesterCourse.getSemester() != null) {
        coursePayload.setSemester(
            SemesterMapper.INSTANCE.toSemesterResponsePayload(semesterCourse.getSemester()));
      }
      User user = auditLogRepository.getCreator(course.getId());
      coursePayload.setAddedBy(UserMapper.INSTANCE.toUserDto(user));

      coursePayloads.add(coursePayload);
    }
    return ResponseBuilder.success("Fetched Pending Courses Successfully", coursePayloads);
  }

  @Override
  public ResponseEntity<ResponseDto> getCourseOnRoles() {
    User currentUser = commonUtility.getLoggedInUser();

    if (currentUser.getNewRole().getRoleType().equals(Role.INSTRUCTOR)) {

      Instructor instructor = Optional.ofNullable(instructorRepository.findByUser(currentUser))
          .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

      List<SemesterCourseInstructor> semesterCourseInstructors = semesterCourseInstructorRepository.findByInstructor(
          instructor);

      if (semesterCourseInstructors.isEmpty()) {
        return ResponseBuilder.getFailedMessage("Data not found");
      }

      List<CoursePayload> coursePayloads = semesterCourseInstructors.stream()
          .map(sci -> {
            CoursePayload coursePayload = CourseMapper.INSTANCE.toCoursePayload(
                sci.getSemesterCourse().getCourse()
            );

            Semester semester = sci.getSemesterCourse().getSemester();
            coursePayload.setSemester(SemesterMapper.INSTANCE.toSemesterResponsePayload(semester));

            List<Enrollment> enrollments = enrollmentRepository.findByCourseId(sci.getSemesterCourse().getCourse().getId());
            if(enrollments.isEmpty()) {
              coursePayload.setIsStudentEnrolled(false);
            }else {
              coursePayload.setIsStudentEnrolled(true);
            }

            return coursePayload;
          })
          .toList();

      return ResponseBuilder.success("Fetched Courses Successfully", coursePayloads);
    } else {

      Student student = Optional.ofNullable(studentRepository.findByUser(currentUser))
          .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
      List<Course> courses = Optional.ofNullable(
              courseRepository.fetchCourseForStudent(student.getId()))
          .orElseThrow(() -> new ResourceNotFoundException("Courses Not Found"));
      return ResponseBuilder.success("Fetched Courses Successfully", CourseMapper.INSTANCE.toCoursePayloadList(courses));

    }

  }

  @Override
  public ResponseEntity<ResponseDto> getCoursesWithNoSemester() {
    List<Course> courses = courseRepository.fetchCourseWithNoSemester();
    if(courses.isEmpty()){
      return ResponseBuilder.getFailedMessage("Courses Not Found");
    }
    return ResponseBuilder.success("Fetched Courses Successfully",
        CourseMapper.INSTANCE.toCoursePayloadList(courses));
  }

  @Override
  public Course addCourse(CoursePayload coursePayload) {
    Course course = CourseMapper.INSTANCE.toCourse(coursePayload);

    course.setCode(CommonUtility.generateCode("CR-"));
    course.setChecked(Checked.PENDING);
    course.setActiveStatus(ActiveStatus.ACTIVE);

    courseRepository.save(course);

    auditLogService.log(commonBeanUtility.getLoggedInUser(), AuditAction.CREATED, "Course",
        course.getId());

    return course;
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

    courseRepository.save(course);

    auditLogService.log(commonBeanUtility.getLoggedInUser(), AuditAction.UPDATED, "Course",
        course.getId());

    return ResponseBuilder.success("Course Updated Successfully", null);


  }
}
