package com.example.mainBase.dto;

import com.example.mainBase.enums.Checked;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursePayload {

  private String code;

  @NotNull(message = "Name cannot be null")
  private String name;

  @Min(value = 0, message = "Credit hours should be more than 0")
  @Max(value = 100, message = "Credit hours should be less than 100")
  private int creditHours;

  @Min(value = 40, message = "Full marks cannot be less than 60")
  @Max(value = 60, message = "Full marks cannot be more than 60")
  private int fullMarks;

  private UserRequestPayload addedBy;

  private Checked checked;

//  @NotNull(message = "At least one instructor is required")
  private List<InstructorPayload> instructors;

  private SemesterPayload semester;
  private Boolean isStudentEnrolled;
}
