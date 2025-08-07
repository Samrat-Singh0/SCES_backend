package com.example.mainBase.service;

import com.example.mainBase.dto.CoursePayload;
import com.example.mainBase.dto.InstructorPayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.dto.SemesterPayload;
import com.example.mainBase.entities.Course;
import com.example.mainBase.entities.Instructor;
import com.example.mainBase.entities.Semester;
import com.example.mainBase.entities.SemesterCourse;
import com.example.mainBase.entities.SemesterCourseInstructor;
import com.example.mainBase.entities.User;
import com.example.mainBase.enums.ActiveStatus;
import com.example.mainBase.enums.AuditAction;
import com.example.mainBase.exception.ResourceNotFoundException;
import com.example.mainBase.mapper.CourseMapper;
import com.example.mainBase.mapper.InstructorMapper;
import com.example.mainBase.mapper.SemesterMapper;
import com.example.mainBase.repository.CourseRepository;
import com.example.mainBase.repository.InstructorRepository;
import com.example.mainBase.repository.SemesterCourseInstructorRepository;
import com.example.mainBase.repository.SemesterCourseRepository;
import com.example.mainBase.repository.SemesterRepository;
import com.example.mainBase.util.CommonBeanUtility;
import com.example.mainBase.util.ResponseBuilder;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
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

  private final CourseService courseService;

  private final InstructorRepository instructorRepository;
  private final SemesterCourseRepository semesterCourseRepository;
  private final SemesterCourseInstructorRepository semesterCourseInstructorRepository;

  @Override
  public ResponseEntity<ResponseDto> getSemester(String label) {
    Semester semester = semesterRepository.findByLabel(label)
        .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));

    SemesterPayload semesterResponse = SemesterMapper.INSTANCE.toSemesterResponsePayload(
        semester);
    return ResponseBuilder.success("Fetched Semester Successfully", semesterResponse);
  }

  @Override
  public ResponseEntity<ResponseDto> getAllSemesters(ActiveStatus status) {
    try {
      SemesterMapper semesterMapper = SemesterMapper.INSTANCE;
      List<Semester> semesters = semesterRepository.findByActiveStatus(status);

      List<SemesterPayload> semesterResponse = semesterMapper.toSemesterResponsePayloadList(
          semesters);

      for (int i = 0; i < semesters.size(); i++) {
        Semester semester = semesters.get(i);
        SemesterPayload payload = semesterResponse.get(i);

        List<CoursePayload> coursePayloads = semester.getSemesterCourses()
            .stream()
            .map(semesterCourse -> {
              CoursePayload coursePayload = CourseMapper.INSTANCE.toCoursePayload(
                  semesterCourse.getCourse());

              List<InstructorPayload> instructors = semesterCourse.getSemesterCourseInstructors()
                  .stream()
                  .map(instructor -> InstructorMapper.INSTANCE.toInstructorPayload(
                      instructor.getInstructor()))
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
    if (optionalSemester.isEmpty()) {
      return ResponseBuilder.getFailedMessage("Semester not found. Please try again later");
    }
    Semester semester = SemesterMapper.INSTANCE.toSemester(payload);

    semester.setId(optionalSemester.get().getId());
    semester.setSemesterOrder(optionalSemester.get().getSemesterOrder());

    List<SemesterCourse> semesterCourses = createSemesterCourse(semester, payload);

    semester.setSemesterCourses(semesterCourses);

    semesterRepository.save(semester);
    User user = commonBeanUtility.getLoggedInUser();
    auditLogService.log(user, AuditAction.CREATED, "Semester", semester.getId());
    return ResponseBuilder.success("Added Semester Successfully",
        SemesterMapper.INSTANCE.toSemesterResponsePayload(semester));
  }

  @Override
  @Transactional
  public ResponseEntity<ResponseDto> updateSemester(
      SemesterPayload semesterPayload) {
    Semester semester = semesterRepository.findByLabel(
            semesterPayload.getLabel())
        .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));

    List<SemesterCourse> semesterCourses = createSemesterCourse(semester, semesterPayload);

    semester.getSemesterCourses().clear();
    semester.getSemesterCourses().addAll(semesterCourses);

//    semester.setSemesterCourses(semesterCourses);

    try {
      semesterRepository.save(semester);
    } catch (Exception error) {
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

  private List<SemesterCourse> createSemesterCourse(Semester semester,
      SemesterPayload semesterPayload) {

    semester.setFee(semesterPayload.getFee());
    semester.setStartDate(semesterPayload.getStartDate());
    semester.setEndDate(semesterPayload.getEndDate());
    semester.setActiveStatus(ActiveStatus.ACTIVE);

//    for (CoursePayload coursePayload : semesterPayload.getCourse()) {
//      Course course = courseRepository.findByCode(coursePayload.getCode());
//
//      if(semester.getSemesterCourses()!=null && !semester.getSemesterCourses().isEmpty()) {
//        for (SemesterCourse semesterCourseData : semester.getSemesterCourses()) {
//          if(course!=null && semesterCourseData.getCourse().getId() == course.getId()) {
//            semesterCourseInstructorRepository.deleteBySemesterCourse_Id(semesterCourseData.getId());
//            semesterCourseRepository.deleteById(semesterCourseData.getId());
//          }
//        }
//      }
//    }

    List<SemesterCourse> semesterCourses = new ArrayList<>();
    for (CoursePayload coursePayload : semesterPayload.getCourse()) {
      Course course = courseRepository.findByCode(coursePayload.getCode());

      SemesterCourse semesterCourse = new SemesterCourse();
      SemesterCourse existingSemesterCourse = null;

      if (semester.getSemesterCourses() != null && !semester.getSemesterCourses().isEmpty()) {
        for (SemesterCourse semesterCourseData : semester.getSemesterCourses()) {
          if (course != null && semesterCourseData.getCourse().getId() == course.getId()) {
            semesterCourse.setId(semesterCourseData.getId());
            existingSemesterCourse = semesterCourseData;
          }
        }
      }

      if (course == null) {
        Course newCourse = courseService.addCourse(coursePayload);
        semesterCourse.setCourse(newCourse);
      } else {
        semesterCourse.setCourse(course);
      }

      semesterCourse.setSemester(semester);

      List<SemesterCourseInstructor> semesterCourseInstructorList = new ArrayList<>();

      for (InstructorPayload instructorPayload : coursePayload.getInstructors()) {
        SemesterCourseInstructor semesterCourseInstructor = new SemesterCourseInstructor();
        Instructor instructor = instructorRepository.findByCode(instructorPayload.getCode())
            .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        if (existingSemesterCourse != null
            && existingSemesterCourse.getSemesterCourseInstructors() != null) {
          for (SemesterCourseInstructor sci : existingSemesterCourse.getSemesterCourseInstructors()) {
            if (sci.getInstructor().getId() == instructor.getId()) {
              semesterCourseInstructor.setId(sci.getId());
            }
          }
        }

        semesterCourseInstructor.setInstructor(instructor);
        semesterCourseInstructor.setSemesterCourse(semesterCourse);
        semesterCourseInstructorList.add(semesterCourseInstructor);
      }

      if (semesterCourse.getSemesterCourseInstructors() == null) {             //semesterCourseInstructor could be null so initializing semesterCourseINstructor
        semesterCourse.setSemesterCourseInstructors(new ArrayList<>());
      }

      semesterCourse.getSemesterCourseInstructors().clear();
      semesterCourse.getSemesterCourseInstructors().addAll(semesterCourseInstructorList);
//      semesterCourse.setSemesterCourseInstructors(semesterCourseInstructorList);

      semesterCourses.add(semesterCourse);
    }
    return semesterCourses;
  }

}