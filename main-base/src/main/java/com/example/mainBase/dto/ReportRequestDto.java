package com.example.mainBase.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReportRequestDto {

  @NotBlank(message = "Document type is required")
  private String documentType;
  private String courseCode;
}
