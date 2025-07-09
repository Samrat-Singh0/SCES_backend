package com.example.mainBase.exception;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.util.ResponseBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ResponseDto> resourceNotFound(ResourceNotFoundException e) {
    return ResponseBuilder.getFailedMessage(e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseDto> handleAllExceptions(Exception ex) {
    log.error(ex);
    return ResponseBuilder.getFailedMessage(ex.getMessage());
  }

}
