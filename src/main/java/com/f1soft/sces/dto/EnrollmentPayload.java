package com.f1soft.sces.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentPayload {

  private SemesterPayload semester;
  private List<CoursePayload> courses;
}
