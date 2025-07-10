package com.example.mainBase.controller;

import com.example.mainBase.dto.FeeAddPayload;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.service.FeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fee")
@RequiredArgsConstructor
public class FeeController {

  private final FeeService feeService;

  @PostMapping("/pay")
  public ResponseEntity<ResponseDto> pay(@RequestBody FeeAddPayload feeAddPayload) {
    return feeService.addFee(feeAddPayload);
  }

  @GetMapping("/get/list/{enrollmentCode}")
  public ResponseEntity<ResponseDto> getFeeList(@PathVariable String enrollmentCode) {
    return feeService.getFeeHistory(enrollmentCode);
  }
}
