package com.example.mainBase.mapper;

import com.example.mainBase.dto.AttendancePayload;
import com.example.mainBase.entities.Attendance;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AttendanceMapper {

  AttendanceMapper INSTANCE = Mappers.getMapper(AttendanceMapper.class);

  Attendance toAttendance(AttendancePayload attendance);

  List<Attendance> toAttendanceList(List<AttendancePayload> attendance);

  AttendancePayload toAttendancePayload(Attendance attendance);

  List<AttendancePayload> toAttendancePayloadList(List<Attendance> attendances);

}
