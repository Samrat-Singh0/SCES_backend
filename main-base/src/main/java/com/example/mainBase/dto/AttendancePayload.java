package com.example.mainBase.dto;

import com.example.attendance_fee_lib.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AttendancePayload {

  private String code;
  private StudentPayload student;
  private CoursePayload course;
  private AttendanceStatus attendanceStatus;
}
