package com.example.mainBase.mapper;

import com.example.mainBase.dto.SemesterPayload;
import com.example.mainBase.entities.Semester;
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
