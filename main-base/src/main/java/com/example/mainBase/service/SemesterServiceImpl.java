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
    if (optionalSemester.isPresent()) {
      return ResponseBuilder.getFailedMessage("Semester already exists");
    }
    Semester semester = SemesterMapper.INSTANCE.toSemester(payload);

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

    List<SemesterCourse> semesterCourses = new ArrayList<>();
    for (CoursePayload coursePayload : semesterPayload.getCourse()) {
      Course course = courseRepository.findByCode(coursePayload.getCode());

      SemesterCourse semesterCourse = new SemesterCourse();
      SemesterCourse existingSemestercourse = null;

      if(course == null) {
        Course newCourse = courseService.addCourse(coursePayload);
        semesterCourse.setCourse(newCourse);
      }else{
        semesterCourse.setCourse(course);
      }

      if (semester.getSemesterCourses() != null && !semester.getSemesterCourses().isEmpty()) {
        for (SemesterCourse semesterCourseData : semester.getSemesterCourses()) {
          if (course != null && semesterCourseData.getCourse().getId() == course.getId()) {
            semesterCourse.setId(semesterCourseData.getId());
            existingSemestercourse = semesterCourseData;
          }
        }
      }

      semesterCourse.setSemester(semester);

      List<SemesterCourseInstructor> semesterCourseInstructorList = new ArrayList<>();

      for (InstructorPayload instructorPayload : coursePayload.getInstructors()) {
        SemesterCourseInstructor semesterCourseInstructor = new SemesterCourseInstructor();
        Instructor instructor = instructorRepository.findByCode(instructorPayload.getCode())
            .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        if (existingSemestercourse != null
            && existingSemestercourse.getSemesterCourseInstructors() != null) {
          for (SemesterCourseInstructor sci : existingSemestercourse.getSemesterCourseInstructors()) {
            if (sci.getInstructor().getId() == instructor.getId()) {
              semesterCourseInstructor.setId(sci.getId());
            }
          }
        }

        semesterCourseInstructor.setInstructor(instructor);
        semesterCourseInstructor.setSemesterCourse(semesterCourse);
        semesterCourseInstructorList.add(semesterCourseInstructor);
      }

      if (semesterCourse.getSemesterCourseInstructors() == null) {                  //semesterCourseInstructor could be null so initializing semtetCourseINstructor
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

//private void updateSemesterCourses(Semester semester, SemesterPayload semesterPayload) {
//
//    semester.setFee(semesterPayload.getFee());
//    semester.setStartDate(semesterPayload.getStartDate());
//    semester.setEndDate(semesterPayload.getEndDate());
//
//    Map<Long, SemesterCourse> existingCourseMap = semester.getSemesterCourses().stream()
//        .collect(Collectors.toMap(sc -> sc.getCourse().getId(), sc -> sc));
//
//    List<SemesterCourse> updatedList = new ArrayList<>();
//
//    for (CoursePayload coursePayload : semesterPayload.getCourse()) {
//      Course course = courseRepository.findByCode(coursePayload.getCode());
//      if (course == null) {
//        throw new RuntimeException("Course not found: " + coursePayload.getCode());
//      }
//
//      SemesterCourse semesterCourse = existingCourseMap.get(course.getId());
//      if (semesterCourse == null) {
//        semesterCourse = new SemesterCourse();
//        semesterCourse.setCourse(course);
//        semesterCourse.setSemester(semester);
//      } else {
//        semesterCourse.getSemesterCourseInstructors().clear();
//      }
//
//      List<SemesterCourseInstructor> instructors = new ArrayList<>();
//      for (InstructorPayload instructorPayload : coursePayload.getInstructors()) {
//        Instructor instructor = instructorRepository.findByCode(instructorPayload.getCode())
//            .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
//
//        SemesterCourseInstructor sci = SemesterCourseInstructor.builder()
//            .semesterCourse(semesterCourse)
//            .instructor(instructor)
//            .build();
//
//        instructors.add(sci);
//      }
//      if (semesterCourse.getSemesterCourseInstructors() == null) {
//        semesterCourse.setSemesterCourseInstructors(new ArrayList<>());
//      } else {
//        semesterCourse.getSemesterCourseInstructors().clear();
//      }
//      semesterCourse.getSemesterCourseInstructors().addAll(instructors);
//
//      updatedList.add(semesterCourse);
//
//      semester.getSemesterCourses().clear();
//      semester.getSemesterCourses().addAll(updatedList);
//    }

//  private List<SemesterCourse> createSemesterCourse(Semester semester, SemesterPayload semesterPayload) {
//
//    semester.setFee(semesterPayload.getFee());
//    semester.setStartDate(semesterPayload.getStartDate());
//    semester.setEndDate(semesterPayload.getEndDate());
//
//    return semesterPayload.getCourse().stream()
//        .map(coursePayload -> {
//          Course course = Optional.ofNullable(courseRepository.findByCode(coursePayload.getCode()))
//              .orElseThrow(() -> new RuntimeException("Course not found: " + coursePayload.getCode()));
//
//          SemesterCourse semesterCourse = new SemesterCourse();
//          SemesterCourse existingSemesterCourse = new  SemesterCourse();
//
//          if(semester.getSemesterCourses()!=null && !semester.getSemesterCourses().isEmpty()){
//            existingSemesterCourse = semester.getSemesterCourses().stream().filter(val -> val.getCourse().getId() == course.getId()).findFirst().orElse(null);
//            if(existingSemesterCourse != null){
//              semesterCourse.setId(existingSemesterCourse.getId());
//            }
//          }
//
//
//          semesterCourse.setCourse(course);
//          semesterCourse.setSemester(semester);
//
//          List<SemesterCourseInstructor> semesterCourseInstructors = new ArrayList<>();
//
//          for(InstructorPayload instructors: coursePayload.getInstructors()) {
//            SemesterCourseInstructor semesterCourseInstructor = new  SemesterCourseInstructor();
//
//            Instructor instructor = instructorRepository.findByCode(instructors.getCode())
//                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
//
//
//
//            if(existingSemesterCourse != null){
//              List<SemesterCourseInstructor> existingSemesterCourseInstructor = existingSemesterCourse.getSemesterCourseInstructors().stream().filter(
//                  val -> val.getInstructor().getId() == instructor.getId()
//              ).toList();
//              if(!existingSemesterCourseInstructor.isEmpty()){
//                semesterCourseInstructor.setId(existingSemesterCourseInstructor.getFirst().getId());
//              }
//            }
//
//
//            semesterCourseInstructor.setInstructor(instructor);
//            semesterCourseInstructor.setSemesterCourse(semesterCourse);
//
//            semesterCourseInstructors.add(semesterCourseInstructor);
//          }
//
//          semesterCourse.setSemesterCourseInstructors(semesterCourseInstructors);
//          return semesterCourse;
//        })
//        .collect(Collectors.toList());
//  }
//
//}






