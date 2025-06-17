package com.f1soft.sces.mapper;

import com.f1soft.sces.dto.EnrollmentPayload;
import com.f1soft.sces.dto.EnrollmentResponsePayload;
import com.f1soft.sces.entities.Enrollment;
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
