package com.f1soft.sces.controller;

import com.f1soft.sces.dto.FeePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.service.FeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<ResponseDto> pay(@RequestBody FeePayload feePayload) {
    return feeService.addFee(feePayload);
  }
}
