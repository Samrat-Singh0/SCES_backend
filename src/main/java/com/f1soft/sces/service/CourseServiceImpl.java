package com.f1soft.sces.service;

import com.f1soft.sces.dto.CoursePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.entities.Course;
import com.f1soft.sces.entities.Instructor;
import com.f1soft.sces.entities.Semester;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.enums.AuditAction;
import com.f1soft.sces.enums.Checked;
import com.f1soft.sces.mapper.CourseMapper;
import com.f1soft.sces.mapper.UserMapper;
import com.f1soft.sces.model.FilterCourse;
import com.f1soft.sces.repository.AuditLogRepository;
import com.f1soft.sces.repository.CourseRepository;
import com.f1soft.sces.repository.InstructorRepository;
import com.f1soft.sces.repository.SemesterRepository;
import com.f1soft.sces.specification.CourseSpecification;
import com.f1soft.sces.util.CommonBeanUtility;
import com.f1soft.sces.util.CommonUtility;
import com.f1soft.sces.util.ResponseBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

  @Override
  public ResponseEntity<ResponseDto> getCourse(String code) {
    Course course = courseRepository.findByCode(code);
    if (course == null) {
      return ResponseBuilder.getFailedMessage("Course not found");
    }

    CoursePayload coursePayload = CourseMapper.INSTANCE.toCoursePayload(course);
    return ResponseBuilder.success("Fetched Course Successfully", coursePayload);
  }

  @Override
  public ResponseEntity<ResponseDto> getAllCourses() {
    try {
      List<Course> courses = courseRepository.findByChecked(Checked.CHECKED);
      if (courses.isEmpty()) {
        return ResponseBuilder.getFailedMessage("No courses found");
      } else {
        List<CoursePayload> courseResponseList = CourseMapper.INSTANCE.toCoursePayloadList(courses);
        return ResponseBuilder.success("Fetched Courses Successfully", courseResponseList);
      }
    } catch (Exception e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }
  }

  @Override
  public ResponseEntity<ResponseDto> getPendingCourses() {
    List<Course> courses = courseRepository.fetchPendingCourses(
        commonUtility.getLoggedInUser().getId());
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
  public ResponseEntity<ResponseDto> addCourse(CoursePayload coursePayload) {
    try {
      Course course = CourseMapper.INSTANCE.toCourse(coursePayload);

      String label = coursePayload.getSemester().getLabel();
      String instructorCode = coursePayload.getInstructor().getCode();

      Optional<Semester> optionalSemester = semesterRepository.findByLabel(label);
      Optional<Instructor> optionalInstructor = instructorRepository.findByCode(
          instructorCode);

      if (optionalInstructor.isEmpty() && optionalSemester.isEmpty()) {
        return ResponseBuilder.getFailedMessage("No semester or instructor found");
      }

      Semester semester = optionalSemester.get();
      Instructor instructor = optionalInstructor.get();

      course.setCode(CommonUtility.generateCode("CR-"));
      course.setSemester(semester);
      course.setInstructor(instructor);
      course.setChecked(Checked.PENDING);

      Course savedCourse = courseRepository.save(course);

      auditLogService.log(commonBeanUtility.getLoggedInUser(), AuditAction.CREATED, "Course",
          savedCourse.getId());

      return ResponseBuilder.success("Course Added Successfully", null);
    } catch (Exception e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }
  }

  @Override
  public ResponseEntity<ResponseDto> deleteCourse(String code) {
    Course course = courseRepository.findByCode(code);
    if (course == null) {
      return ResponseBuilder.getFailedMessage("Course Not Found");
    }
    courseRepository.deleteById(course.getId());

    User loggedInUser = commonBeanUtility.getLoggedInUser();
    auditLogService.log(loggedInUser, AuditAction.DELETED, "Course", course.getId());

    return ResponseBuilder.success("Course Deleted Successfully", null);
  }

  @Override
  public ResponseEntity<ResponseDto> updateCourse(CoursePayload payload) {
    Course course = courseRepository.findByCode(payload.getCode());
    if (course == null) {
      return ResponseBuilder.getFailedMessage("Course Not Found");
    }

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

    auditLogService.log(commonBeanUtility.getLoggedInUser(), AuditAction.UPDATED, "Course",
        course.getId());

    courseRepository.save(course);
    return ResponseBuilder.success("Course Updated Successfully", null);

  }

  @Override
  public ResponseEntity<ResponseDto> getCourseBySearchText(FilterCourse filterCriteria) {
    try {
      List<Course> courses = courseRepository.findByChecked(Checked.CHECKED);
      if (courses.isEmpty()) {
        return ResponseBuilder.getFailedMessage("No courses found");
      }

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
    } catch (Exception e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }
  }


}
