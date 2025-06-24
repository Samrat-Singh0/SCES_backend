package com.example.mainBase.mapper;

import com.example.mainBase.dto.StudentPayload;
import com.example.mainBase.entities.Student;
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
