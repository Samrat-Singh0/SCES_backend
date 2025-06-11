package com.f1soft.sces.service;

import com.f1soft.sces.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface InstructorService {


  ResponseEntity<ResponseDto> getAllInstructors();
}
