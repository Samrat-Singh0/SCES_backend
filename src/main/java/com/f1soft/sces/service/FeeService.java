package com.f1soft.sces.service;

import com.f1soft.sces.dto.FeePayload;
import com.f1soft.sces.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface FeeService {

  ResponseEntity<ResponseDto> addFee(FeePayload payload);

}
