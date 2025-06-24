package com.f1soft.sces.builder;

import com.f1soft.sces.dto.AttendancePayload;
import com.f1soft.sces.entities.Attendance;
import com.f1soft.sces.entities.Course;
import com.f1soft.sces.entities.Enrollment;
import com.f1soft.sces.entities.Student;
import com.f1soft.sces.util.CommonUtility;
import java.time.LocalDate;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AttendanceBuilder {

  public Attendance build(AttendancePayload payload, Student student, Course course,
      Enrollment enrollment) {
    return Attendance.builder()
        .code(CommonUtility.generateCode("AT-"))
        .student(student)
        .course(course)
        .enrollment(enrollment)
        .date(LocalDate.now())
        .attendanceStatus(payload.getAttendanceStatus())
        .build();
  }
}
