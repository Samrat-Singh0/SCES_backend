package com.example.mainBase.mapper;

import com.example.mainBase.dto.EnrollmentPayload;
import com.example.mainBase.dto.EnrollmentResponsePayload;
import com.example.mainBase.entities.Enrollment;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper()
public interface EnrollmentMapper {

  EnrollmentMapper INSTANCE = Mappers.getMapper(EnrollmentMapper.class);

  Enrollment toEnrollment(EnrollmentPayload payload);
  
  EnrollmentResponsePayload toResponsePayload(Enrollment enrollment);

  default Page<EnrollmentResponsePayload> toEnrollmentResponsePayloadPage(Page<Enrollment> enrollmentPage) {
    List<EnrollmentResponsePayload> enrollmentResponsePayloadList = toResponsePayloads(enrollmentPage.getContent());
    return new PageImpl<>(enrollmentResponsePayloadList, enrollmentPage.getPageable(), enrollmentPage.getTotalElements());
  }

  List<EnrollmentResponsePayload> toResponsePayloads(List<Enrollment> enrollments);

}
