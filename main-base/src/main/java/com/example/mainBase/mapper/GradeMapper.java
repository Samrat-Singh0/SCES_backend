package com.example.mainBase.mapper;

import com.example.mainBase.dto.GradePayload;
import com.example.mainBase.entities.Grade;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GradeMapper {

  GradeMapper INSTANCE = Mappers.getMapper(GradeMapper.class);

  Grade toGrade(GradePayload payload);

  GradePayload toPayload(Grade grade);

  List<Grade> toGrades(List<GradePayload> payloads);

  List<GradePayload> toPayloads(List<Grade> grades);
}
