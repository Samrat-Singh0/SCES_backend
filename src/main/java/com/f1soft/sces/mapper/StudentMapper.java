package com.f1soft.sces.mapper;

import com.f1soft.sces.dto.StudentPayload;
import com.f1soft.sces.entities.Student;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentMapper {

  StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

  StudentPayload toStudentPayload(Student student);

  List<StudentPayload> toStudentPayloadList(List<Student> students);

  Student toStudent(StudentPayload payload);

  List<Student> toStudentList(List<StudentPayload> payloads);
  
}
