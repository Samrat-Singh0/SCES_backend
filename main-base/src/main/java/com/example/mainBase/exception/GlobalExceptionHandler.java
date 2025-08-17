package com.example.mainBase.exception;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.util.ResponseBuilder;
import java.lang.NullPointerException;
import java.time.format.DateTimeParseException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ResponseDto> resourceNotFound(ResourceNotFoundException e) {
    log.error(e);
    return ResponseBuilder.getFailedMessage(e.getMessage());
  }

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<ResponseDto> nullPointer(NullPointerException e) {
    log.error(e);
    return ResponseBuilder.getFailedMessage("Data cannot be null");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseDto> methodArgumentNotValid(MethodArgumentNotValidException e) {
    log.error(e);
    return ResponseBuilder.getFailedMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
  }

  @ExceptionHandler(DateTimeParseException.class)
  public ResponseEntity<ResponseDto> dateTimeParse(DateTimeParseException e) {
    log.error(e);
    return ResponseBuilder.getFailedMessage(e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseDto> handleAllExceptions(Exception e) {
    log.error(e);
    return ResponseBuilder.getFailedMessage("Oops! Something went wrong");
  }
}
