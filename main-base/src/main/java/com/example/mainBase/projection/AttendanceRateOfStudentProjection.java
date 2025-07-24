package com.example.mainBase.projection;

import java.time.LocalDate;

public interface AttendanceRateOfStudentProjection {
  String getStudentCode();
  Long getPresentDays();
  Long getTotalDays();
  LocalDate getStartDate();
}
