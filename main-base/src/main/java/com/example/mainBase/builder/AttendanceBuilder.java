package com.example.mainBase.builder;

import com.example.mainBase.dto.AttendancePayload;
import com.example.mainBase.entities.Attendance;
import com.example.mainBase.entities.Course;
import com.example.mainBase.entities.Enrollment;
import com.example.mainBase.entities.Student;
import com.example.mainBase.util.CommonUtility;
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
