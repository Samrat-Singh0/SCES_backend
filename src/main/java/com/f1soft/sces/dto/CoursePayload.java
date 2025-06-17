package com.f1soft.sces.dto;

import com.f1soft.sces.enums.Checked;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursePayload {

  private String code;
  private String name;
  private int creditHours;
  private int fullMarks;
  private SemesterPayload semester;
  private InstructorPayload instructor;
  private UserRequestPayload addedBy;
  private Checked checked;
}
