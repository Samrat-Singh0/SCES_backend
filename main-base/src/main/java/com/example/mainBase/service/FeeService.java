package com.example.mainBase.service;

import com.example.mainBase.dto.FeeAddPayload;
import com.example.mainBase.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface FeeService {

  ResponseEntity<ResponseDto> getFeeHistory(String enrollmentCode);

  ResponseEntity<ResponseDto> addFee(FeeAddPayload payload);

}
