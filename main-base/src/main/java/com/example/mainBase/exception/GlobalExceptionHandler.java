package com.example.mainBase.exception;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.util.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ResponseDto> resourceNotFound(ResourceNotFoundException e) {
    return ResponseBuilder.getFailedMessage(e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseDto> handleAllExceptions(Exception ex) {
    return ResponseBuilder.getFailedMessage(ex.getMessage());
  }

}
