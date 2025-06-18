package com.f1soft.sces.mapper;

import com.f1soft.sces.dto.GradePayload;
import com.f1soft.sces.entities.Grade;
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
