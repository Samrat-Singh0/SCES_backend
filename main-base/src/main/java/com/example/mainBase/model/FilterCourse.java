package com.example.mainBase.model;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterCourse {

  @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Invalid content type")
  private String name;

  @Pattern(regexp = "^[A-Za-z\\s]+$")
  private String instructor;

  @Pattern(regexp = "^[A-Za-z]+$")
  private String semester;
}
