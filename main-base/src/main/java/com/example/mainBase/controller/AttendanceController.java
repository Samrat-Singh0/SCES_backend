package com.example.mainBase.controller;

import com.example.mainBase.dto.AttendancePayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.service.AttendanceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

  private final AttendanceService attendanceService;

  @GetMapping("/get/date")
  public ResponseEntity<ResponseDto> getAttendanceOfDate(@RequestParam String courseCode,
      @RequestParam String date) {
    return attendanceService.getAttendanceOfDate(courseCode, date);
  }

  @PostMapping("/save")
  @PreAuthorize("hasRole(@securityRoles.INSTRUCTOR)")
  public ResponseEntity<ResponseDto> saveAttendance(@RequestBody List<AttendancePayload> payload) {
    return attendanceService.addAttendance(payload);
  }

  @GetMapping("/rate/{courseCode}")
  public ResponseEntity<ResponseDto> getAttendanceRate(@PathVariable String courseCode) {
    return attendanceService.getTotalAttendance(courseCode);
  }
}
