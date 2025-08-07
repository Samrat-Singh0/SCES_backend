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
public class FilterEnrollment {

  @Pattern(regexp = "^[A-Za-z\\s]+$\n")         //space and alphabets
  private String studentName;

  @Pattern(regexp = "^[A-Za-z]+$")
  private String semester;

  @Pattern(regexp = "^[A-Za-z]+$")
  private String payStatus;
}
