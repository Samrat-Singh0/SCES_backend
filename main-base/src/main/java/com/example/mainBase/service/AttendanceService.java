package com.example.mainBase.service;

import com.example.mainBase.dto.AttendancePayload;
import com.example.mainBase.dto.ResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface AttendanceService {
  
  ResponseEntity<ResponseDto> getAttendanceOfDate(String courseCode, String date);

  ResponseEntity<ResponseDto> addAttendance(List<AttendancePayload> payload);

}
