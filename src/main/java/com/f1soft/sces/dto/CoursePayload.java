package com.f1soft.sces.dto;

import com.f1soft.sces.entities.Instructor;
import com.f1soft.sces.entities.Semester;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursePayload {

  private String name;
  private int creditHours;
  private int fullMarks;
  private Semester semester;
  private Instructor instructor;
}
