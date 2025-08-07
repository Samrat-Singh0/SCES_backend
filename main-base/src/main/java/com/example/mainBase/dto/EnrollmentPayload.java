package com.example.mainBase.dto;

import jakarta.validation.constraints.NotNull;
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

  @NotNull(message = "Semester is required")
  private SemesterPayload semester;

//  @NotNull(message = "Please add at least one course")
  private List<CoursePayload> courses;
}
