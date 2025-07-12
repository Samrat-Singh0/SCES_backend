package com.example.mainBase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterEnrollment {
  private String studentName;
  private String semester;
  private String payStatus;
}
