package com.f1soft.sces.dto;

import com.f1soft.sces.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstructorPayload {

  String instructorCode;
  User user;
}
