package com.example.mainBase.service;

import com.example.mainBase.dto.FeePayload;
import com.example.mainBase.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface FeeService {

  ResponseEntity<ResponseDto> addFee(FeePayload payload);

}
