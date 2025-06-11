package com.f1soft.sces.service;

import com.f1soft.sces.dto.CoursePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.entities.Course;
import com.f1soft.sces.entities.Instructor;
import com.f1soft.sces.entities.Semester;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.mapper.CourseMapper;
import com.f1soft.sces.repository.CourseRepository;
import com.f1soft.sces.repository.InstructorRepository;
import com.f1soft.sces.repository.SemesterRepository;
import com.f1soft.sces.util.CommonBeanUtility;
import com.f1soft.sces.util.ResponseBuilder;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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


  @Override
  public ResponseEntity<ResponseDto> getAllCourses() {
    try {
      List<Course> courses = courseRepository.findAll();
      if (courses.isEmpty()) {
        return ResponseBuilder.getFailedMessage("No courses found");
      } else {
        List<CoursePayload> courseResponseList = CourseMapper.INSTANCE.toCoursePayloadList(courses);
        return ResponseBuilder.success("Fetched Courses Successfully", courses);
      }
    } catch (Exception e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }
  }

  @Override
  public ResponseEntity<ResponseDto> addCourse(CoursePayload coursePayload) {
    try {
      Course course = CourseMapper.INSTANCE.toCourse(coursePayload);

      String label = coursePayload.getSemester().getLabel();
      String instructorCode = coursePayload.getInstructor().getInstructorCode();

      Optional<Semester> optionalSemester = semesterRepository.findByLabel(label);
      Optional<Instructor> optionalInstructor = instructorRepository.findByInstructorCode(
          instructorCode);

      if (optionalInstructor.isEmpty() && optionalSemester.isEmpty()) {
        return ResponseBuilder.getFailedMessage("No semester or instructor found");
      }

      Semester semester = optionalSemester.get();
      Instructor instructor = optionalInstructor.get();

      course.setSemester(semester);
      course.setInstructor(instructor);

      Course savedCourse = courseRepository.save(course);

      User loggedInUser = commonBeanUtility.getLoggedUserId();
      auditLogService.log(loggedInUser, "Created", "Course", savedCourse.getId());

      return ResponseBuilder.success("Course Added Successfully", null);
    } catch (Exception e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }
  }


}
