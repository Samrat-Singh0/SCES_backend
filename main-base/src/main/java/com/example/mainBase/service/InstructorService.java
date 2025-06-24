package com.example.mainBase.service;

import com.example.mainBase.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface InstructorService {


  ResponseEntity<ResponseDto> getAllInstructors();
}
