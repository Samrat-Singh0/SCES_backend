package com.example.mainBase.mapper;

import com.example.mainBase.dto.InstructorPayload;
import com.example.mainBase.entities.Instructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InstructorMapper {

  InstructorMapper INSTANCE = Mappers.getMapper(InstructorMapper.class);

  Instructor toInstructor(InstructorPayload payload);

  InstructorPayload toInstructorPayload(Instructor instructor);
}
