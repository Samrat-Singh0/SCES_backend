package com.f1soft.sces.mapper;

import com.f1soft.sces.dto.SemesterDto;
import com.f1soft.sces.entities.Semester;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SemesterMapper {
  SemesterMapper INSTANCE = Mappers.getMapper(SemesterMapper.class);

  SemesterDto toSemesterDto(Semester semester);
  List<SemesterDto> toSemesterDtoList(List<Semester> semesters);
  Semester toSemester(SemesterDto semesterDto);

}
