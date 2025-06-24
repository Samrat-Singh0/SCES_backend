package com.f1soft.sces.mapper;

import com.f1soft.sces.dto.AttendancePayload;
import com.f1soft.sces.entities.Attendance;
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
