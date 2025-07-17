package com.example.mainBase.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.attendance_fee_lib.enums.FeeStatus;
import com.example.attendance_fee_lib.services.LibFeeService;
import com.example.mainBase.builder.EnrollmentBuilder;
import com.example.mainBase.dto.CoursePayload;
import com.example.mainBase.dto.EnrollmentPayload;
import com.example.mainBase.dto.EnrollmentResponsePayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.dto.SemesterPayload;
import com.example.mainBase.dto.StudentPayload;
import com.example.mainBase.entities.Course;
import com.example.mainBase.entities.Enrollment;
import com.example.mainBase.entities.Instructor;
import com.example.mainBase.entities.Semester;
import com.example.mainBase.entities.Student;
import com.example.mainBase.entities.User;
import com.example.mainBase.enums.ActiveStatus;
import com.example.mainBase.enums.AuditAction;
import com.example.mainBase.enums.Checked;
import com.example.mainBase.enums.CompletionStatus;
import com.example.mainBase.enums.EnrollStatus;
import com.example.mainBase.enums.Role;
import com.example.mainBase.repository.CourseRepository;
import com.example.mainBase.repository.EnrollmentRepository;
import com.example.mainBase.repository.SemesterRepository;
import com.example.mainBase.repository.StudentRepository;
import com.example.mainBase.util.CommonBeanUtility;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceImplTest {

  @Mock
  private CommonBeanUtility commonBeanUtility;

  @Mock
  private StudentRepository studentRepository;

  @Mock
  private EnrollmentRepository enrollmentRepository;

  @Mock
  private AuditLogService auditLogService;

  @Mock
  private CourseRepository courseRepository;

  @Mock
  private SemesterRepository semesterRepository;

  @Mock
  private LibFeeService libFeeService;

  @InjectMocks
  private EnrollmentServiceImpl enrollmentService;

  @Test
  void getEnrollmentForStudentTest() {

    User user = getUser();
    user.setRole(Role.STUDENT);

    Student student = getStudent();

    Enrollment enrollment = new Enrollment();
    List<Enrollment> enrollments = List.of(enrollment);

    when(commonBeanUtility.getLoggedInUser()).thenReturn(user);
    when(studentRepository.findByUser(any(User.class))).thenReturn(student);
    when(enrollmentRepository.findEnrollmentForStudent(anyLong())).thenReturn(enrollments);

    ResponseEntity<ResponseDto> response = enrollmentService.getEnrollments();
    assertResponse(response);

    verify(commonBeanUtility).getLoggedInUser();
    verify(studentRepository).findByUser(user);
    verify(enrollmentRepository).findEnrollmentForStudent(student.getId());
  }

  @Test
  void getEnrollmentForInstructorTest() {
    User user = getUser();
    user.setRole(Role.SUPER_ADMIN);

    Enrollment enrollment = new Enrollment();
    List<Enrollment> enrollments = List.of(enrollment);

    when(commonBeanUtility.getLoggedInUser()).thenReturn(user);
    when(enrollmentRepository.findByCompletionStatusNot(any())).thenReturn(enrollments);

    ResponseEntity<ResponseDto> response = enrollmentService.getEnrollments();

    assertResponse(response);
  }


  @Test
  void getPendingEnrollmentsTest() {
    Enrollment enrollment = new Enrollment();
    List<Enrollment> enrollments = List.of(enrollment);

    when(enrollmentRepository.findByCompletionStatus(any())).thenReturn(enrollments);

    ResponseEntity<ResponseDto> response = enrollmentService.getPendingEnrollments();

    assertResponse(response);
  }

  @Test
  void updateEnrollmentTest() {
    Enrollment existingEnrollment = getEnrollment();

    Student student = getStudent();

    EnrollmentResponsePayload payload = new EnrollmentResponsePayload();
    payload.setCode("ENCODE");
    payload.setCompletionStatus(CompletionStatus.RUNNING);

    StudentPayload studentPayload = new StudentPayload();
    studentPayload.setCode("STUCODE");
    payload.setStudent(studentPayload);

    when(enrollmentRepository.findByCode(any())).thenReturn(existingEnrollment);
    when(studentRepository.findByCode(any())).thenReturn(student);
    when(studentRepository.save(any(Student.class))).thenReturn(student);
    when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(existingEnrollment);

    doNothing().when(auditLogService).log(any(), any(),any(), any());

    ResponseEntity<ResponseDto> response = enrollmentService.update(payload);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    assertEquals("Enrollment Updated Successfully", response.getBody().getMessage());
    assertEquals(EnrollStatus.ENROLLED, student.getEnrollStatus());

    verify(enrollmentRepository).findByCode(any());
    verify(studentRepository).findByCode(any());
    verify(studentRepository).save(any(Student.class));
    verify(enrollmentRepository).save(any(Enrollment.class));
    verify(auditLogService).log(any(), eq(AuditAction.UPDATED),eq("Enrollment"), eq(existingEnrollment.getId()));
  }

  @Test
  void currentlyEnrollingStudentTest() {
    EnrollmentPayload enrollmentPayload = new EnrollmentPayload();
    enrollmentPayload.setSemester(getSemesterPayload());

    User user = getUser();
    user.setRole(Role.STUDENT);
    when(commonBeanUtility.getLoggedInUser()).thenReturn(user);

    Student student = getStudent();
    student.setUser(user);
    student.setEnrollStatus(EnrollStatus.ENROLLED);
    when(studentRepository.findByUser(any(User.class))).thenReturn(student);

    Semester semester = getSemester();
    when(semesterRepository.findByLabel(anyString())).thenReturn(Optional.of(semester));

    Enrollment existingEnrollment = new Enrollment();
    List<Enrollment> existingEnrollments = List.of(existingEnrollment);
    when(enrollmentRepository.findByStudent_idAndCompletionStatus(anyLong(), any(CompletionStatus.class))).thenReturn(existingEnrollments);


    ResponseEntity<ResponseDto> response = enrollmentService.addEnrollment(enrollmentPayload);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("You are currently enrolled to an existing course.", response.getBody().getMessage());

    verify(enrollmentRepository).findByStudent_idAndCompletionStatus(anyLong(), any(CompletionStatus.class));
  }

  @Test
  void addEnrollmentSuccessTest() {
    EnrollmentPayload enrollmentPayload = new EnrollmentPayload();
    enrollmentPayload.setSemester(getSemesterPayload());

    CoursePayload coursePayload = new CoursePayload();
    coursePayload.setCode("COURSE");
    enrollmentPayload.setCourses(List.of(coursePayload));

    User user = getUser();
    user.setRole(Role.STUDENT);
    when(commonBeanUtility.getLoggedInUser()).thenReturn(user);

    Student student = getStudent();
    student.setUser(user);
    when(studentRepository.findByUser(user)).thenReturn(student);

    Semester semester = getSemester();
    when(semesterRepository.findByLabel(anyString())).thenReturn(Optional.of(semester));

    when(enrollmentRepository.findByStudent_idAndCompletionStatus(student.getId(), CompletionStatus.RUNNING))
        .thenReturn(List.of());

    Enrollment completedEnrollment = new Enrollment();
    completedEnrollment.setOutstandingFee(new BigDecimal("100.00"));
    completedEnrollment.setSemester(Semester.builder().label("Fall 2023").build());
    when(enrollmentRepository.findByStudent_idAndCompletionStatus(student.getId(), CompletionStatus.COMPLETED))
        .thenReturn(List.of(completedEnrollment));

    when(libFeeService.checkThreshold(any(), any())).thenReturn(false);

    Course course = getCourse();
    when(courseRepository.findByCode("COURSE")).thenReturn(course);

    when(enrollmentRepository.findAllByStudent_Id(student.getId())).thenReturn(List.of(new Enrollment()));

    Enrollment builtEnrollment = getEnrollment();
    when(enrollmentRepository.save(any())).thenReturn(builtEnrollment);

    try (MockedStatic<EnrollmentBuilder> mockedBuilder = mockStatic(EnrollmentBuilder.class)) {
      mockedBuilder.when(() -> EnrollmentBuilder.build(any(), any(), any())).thenReturn(builtEnrollment);

      ResponseEntity<ResponseDto> response = enrollmentService.addEnrollment(enrollmentPayload);

      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertNotNull(response.getBody());
      assertEquals("Enrollment Added Successfully", response.getBody().getMessage());

      verify(auditLogService).log(any(), any(), any(), any());
    }
  }



  void assertResponse(ResponseEntity<ResponseDto> response) {

    EnrollmentResponsePayload enrollmentPayload = new EnrollmentResponsePayload();
    List<EnrollmentResponsePayload> enrollmentPayloadList = List.of(enrollmentPayload);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Fetched Enrollments Successfully", response.getBody().getMessage());
    assertEquals(enrollmentPayloadList, response.getBody().getBody());
  }

  private User getUser() {
    return User.builder()
        .id(1L)
        .code("UCODE")
        .firstName("John")
        .middleName("K.")
        .lastName("Doe")
        .email("johndoe@email.com")
        .password("securePassword123")
        .address("123 Main St, Cityville")
        .role(Role.STUDENT)
        .mustChangePassword(false)
        .activeStatus(ActiveStatus.ACTIVE)
        .remarks("Mock user for testing.")
        .build();
  }

  private Student getStudent() {
    return Student.builder()
        .id(100L)
        .code("STUCODE")
        .enrollStatus(EnrollStatus.NEW)
        .user(getUser())
        .build();
  }

  private Enrollment getEnrollment() {
    return Enrollment.builder()
        .id(100L)
        .code("ENCODE")
        .student(getStudent())
        .semester(getSemester())
        .enrolledDate(LocalDate.of(2024, 2, 10))
        .completionDate(LocalDate.of(2024, 12, 15))
        .completionStatus(CompletionStatus.RUNNING)
        .outstandingFee(new BigDecimal("500.00"))
        .paidStatus(FeeStatus.UNPAID)
        .build();
  }

  private Semester getSemester() {
    return Semester.builder()
        .id(200L)
        .label("Spring 2024")
        .fee(new BigDecimal("2000.00"))
        .startDate(LocalDate.of(2024, 2, 1))
        .endDate(LocalDate.of(2024, 6, 30))
        .build();
  }

  private Course getCourse() {
    return Course.builder()
        .id(100L)
        .code("COURSE")
        .name("Spring 2024")
        .creditHours(90)
        .fullMarks(60)
//        .semester(getSemester())
//        .instructor(getInstructor())
        .checked(Checked.CHECKED)
        .activeStatus(ActiveStatus.ACTIVE)
        .remarks("Mock user for testing.")
        .build();
  }

  private Instructor getInstructor() {
    return Instructor.builder()
        .id(100L)
        .code("INSTRUCTOR")
        .user(getUser())
        .build();
  }

  private SemesterPayload getSemesterPayload() {
    return SemesterPayload.builder()
        .label("Spring 2024")
        .fee(new BigDecimal("2000.00"))
        .startDate(LocalDate.of(2024, 2, 1))
        .endDate(LocalDate.of(2024, 6, 30))
        .build();
  }

}
