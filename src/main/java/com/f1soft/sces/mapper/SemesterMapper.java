package com.f1soft.sces.mapper;

import com.f1soft.sces.dto.SemesterPayload;
import com.f1soft.sces.entities.Semester;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SemesterMapper {

  SemesterMapper INSTANCE = Mappers.getMapper(SemesterMapper.class);

  SemesterPayload toSemesterResponsePayload(Semester semester);

  List<SemesterPayload> toSemesterResponsePayloadList(List<Semester> semesters);

  Semester toSemester(SemesterPayload semesterPayload);

}
