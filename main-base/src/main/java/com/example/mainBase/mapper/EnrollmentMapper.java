package com.example.mainBase.mapper;

import com.example.mainBase.dto.EnrollmentPayload;
import com.example.mainBase.dto.EnrollmentResponsePayload;
import com.example.mainBase.entities.Enrollment;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface EnrollmentMapper {

  EnrollmentMapper INSTANCE = Mappers.getMapper(EnrollmentMapper.class);

  Enrollment toEnrollment(EnrollmentPayload payload);
  
  EnrollmentResponsePayload toResponsePayload(Enrollment enrollment);

  List<EnrollmentResponsePayload> toResponsePayloads(List<Enrollment> enrollments);

}
