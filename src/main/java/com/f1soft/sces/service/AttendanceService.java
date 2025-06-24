package com.f1soft.sces.service;

import com.f1soft.sces.dto.AttendancePayload;
import com.f1soft.sces.dto.ResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface AttendanceService {
  
  ResponseEntity<ResponseDto> getAttendanceOfDate(String courseCode, String date);

  ResponseEntity<ResponseDto> addAttendance(List<AttendancePayload> payload);

}
